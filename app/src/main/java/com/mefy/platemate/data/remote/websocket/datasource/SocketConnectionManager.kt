package com.mefy.platemate.data.remote.websocket.datasource

import android.util.Log
import com.mefy.platemate.BuildConfig
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.language.LanguageProvider
import com.mefy.platemate.domain.model.socket.SocketConnectionState
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlin.coroutines.resume
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject

@Singleton
class SocketConnectionManager @Inject constructor(
    @param:Named("WebSocketEndpoint") private val socketEndpoint: String,
    private val socketAuthTokenProvider: SocketAuthTokenProvider,
    private val appDispatchers: AppDispatchers,
    private val languageProvider: LanguageProvider
) {

    private var socket: Socket? = null
    private val connectionMutex = Mutex()
    private val connectionState = MutableStateFlow(SocketConnectionState.DISCONNECTED)

    // Bağlantı hatasında token yenileyip soketi yeniden kurmak için kendi kapsamımız.
    private val scope = CoroutineScope(appDispatchers.io + SupervisorJob())
    @Volatile private var lastReauthAt = 0L

    // Aktif event abonelikleri — tek bir Socket örneğine değil buraya bağlanır; her (yeniden)
    // kurulan sokete buradakilerin tümü yeniden iliştirilir. Böylece reauth/reconnect canlı
    // akışları (new_message, delivered, read, presence) koparmaz.
    private val eventListeners = ConcurrentHashMap<String, MutableSet<Emitter.Listener>>()

    suspend fun connect() {
        withContext(appDispatchers.io) {
            ensureConnected()
        }
    }

    suspend fun disconnect() {
        withContext(appDispatchers.io) {
            connectionMutex.withLock {
                socket?.disconnect()
                socket?.off()
                socket = null
                connectionState.value = SocketConnectionState.DISCONNECTED
            }
        }
    }

    fun isConnected(): Boolean = socket?.connected() ?: false

    fun observeConnectionState(): StateFlow<SocketConnectionState> = connectionState.asStateFlow()

    suspend fun emit(eventName: String, payload: JSONObject) {
        withContext(appDispatchers.io) {
            val currentSocket = ensureConnected() ?: return@withContext
            currentSocket.emit(eventName, payload)
        }
    }

    // Scalar payload variant — some backend listeners expect a raw value (e.g. join_room → Long)
    // rather than a JSON object.
    suspend fun emit(eventName: String, value: Any) {
        withContext(appDispatchers.io) {
            val currentSocket = ensureConnected() ?: return@withContext
            currentSocket.emit(eventName, value)
        }
    }

    // Ack-aware emit: resolves with the server's ack payload, or null if the socket is
    // unavailable or [timeoutMs] elapses with no ack (the two cases the caller distinguishes by
    // checking connectivity first, same as the plain [emit] above).
    suspend fun emitWithAck(eventName: String, payload: JSONObject, timeoutMs: Long = 30_000L): JSONObject? =
        withContext(appDispatchers.io) {
            val currentSocket = ensureConnected() ?: return@withContext null
            withTimeoutOrNull(timeoutMs) {
                suspendCancellableCoroutine { cont ->
                    currentSocket.emit(eventName, payload, Ack { args ->
                        if (cont.isActive) cont.resume(args.firstOrNull()?.toJsonObjectOrNull())
                    })
                }
            }
        }

    fun observeEvent(eventName: String): Flow<JSONObject> = callbackFlow {
        val listener = Emitter.Listener { args ->
            if (BuildConfig.DEBUG) Log.d(TAG, "event '$eventName' received: ${args.firstOrNull()}")
            args.firstOrNull()
                ?.toJsonObjectOrNull()
                ?.let { trySend(it) }
        }

        // Listener'ı sokete değil deftere bağla; ensureConnected mevcut sokete iliştirir, sonraki
        // her yeniden kurulum [reattachEventListeners] ile geri bağlar → reconnect'te kopmaz.
        registerEventListener(eventName, listener)
        withContext(appDispatchers.io) { ensureConnected() }

        awaitClose { unregisterEventListener(eventName, listener) }
    }

    private fun registerEventListener(eventName: String, listener: Emitter.Listener) {
        eventListeners.getOrPut(eventName) { Collections.synchronizedSet(mutableSetOf()) }
            .add(listener)
        socket?.on(eventName, listener)
    }

    private fun unregisterEventListener(eventName: String, listener: Emitter.Listener) {
        eventListeners[eventName]?.remove(listener)
        socket?.off(eventName, listener)
    }

    // Defterdeki tüm event listener'larını verilen (yeni kurulan) sokete bağlar.
    private fun reattachEventListeners(target: Socket) {
        eventListeners.forEach { (eventName, listeners) ->
            synchronized(listeners) {
                listeners.forEach { target.on(eventName, it) }
            }
        }
    }

    private fun registerConnectionListeners(currentSocket: Socket) {
        currentSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            connectionState.value = SocketConnectionState.CONNECTED
            Log.i(TAG, "Socket connected: $socketEndpoint")
        })
        currentSocket.on(Socket.EVENT_DISCONNECT, Emitter.Listener { args ->
            connectionState.value = SocketConnectionState.DISCONNECTED
            Log.w(TAG, "Socket disconnected: ${args.firstOrNull()}")
        })
        currentSocket.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener { args ->
            connectionState.value = SocketConnectionState.ERROR
            // En sık neden: süresi dolmuş access token → handshake 401 → "xhr poll error".
            // Token'ı yenileyip soketi taze token ile yeniden kurarak kendini onar.
            Log.e(TAG, "Socket connect error: ${args.firstOrNull()}")
            scope.launch { reauthAndReconnect() }
        })
    }

    private fun Any.toJsonObjectOrNull(): JSONObject? = when (this) {
        is JSONObject -> this
        is String -> runCatching { JSONObject(this) }.getOrNull()
        else -> null
    }

    private suspend fun ensureConnected(): Socket? =
        connectionMutex.withLock {
            socket?.let { return@withLock it }

            // Her el sıkışma GEÇERLİ token ister; gerekiyorsa burada yenilenir.
            val token = socketAuthTokenProvider.validAccessTokenOrNull()
            if (token.isNullOrBlank()) {
                connectionState.value = SocketConnectionState.UNAUTHORIZED
                return@withLock null
            }

            connectionState.value = SocketConnectionState.CONNECTING
            socket = buildSocket(token)
            socket
        }

    /**
     * Bağlantı hatasında (çoğunlukla süresi dolmuş token) bir defalık — kısa süreli debounce ile —
     * token yenileyip soketi taze query ile yeniden kurar. Yenileme başarısızsa [UNAUTHORIZED]'a
     * geçip durur; böylece sunucu gerçekten kapalıyken sonsuz yeniden-kurma fırtınası olmaz.
     */
    private suspend fun reauthAndReconnect() {
        val now = System.currentTimeMillis()
        if (now - lastReauthAt < REAUTH_DEBOUNCE_MS) return
        lastReauthAt = now

        connectionMutex.withLock {
            val current = socket ?: return
            val freshToken = socketAuthTokenProvider.validAccessTokenOrNull()

            // Eski soketin kütüphane-içi yeniden bağlanmasını tamamen durdur.
            current.off()
            current.disconnect()
            socket = null

            if (freshToken.isNullOrBlank()) {
                connectionState.value = SocketConnectionState.UNAUTHORIZED
                return
            }

            connectionState.value = SocketConnectionState.CONNECTING
            socket = buildSocket(freshToken)
        }
    }

    /** Verilen token ile soketi kurar ve bağlanır. Senkron hata olursa null döner. */
    private fun buildSocket(token: String): Socket? =
        runCatching {
            // REST tarafında LanguageInterceptor aynısını yapıyor; soket handshake'i de aynı
            // sinyali taşımazsa backend'in "error" event'i (limit/engel/kapalı/red mesajları)
            // her zaman backend'in varsayılan dilinde gelir, uygulamanın dil ayarını yok sayar.
            val options = IO.Options.builder()
                .setQuery("token=$token")
                .setExtraHeaders(
                    mapOf(ACCEPT_LANGUAGE_HEADER to listOf(languageProvider.getAcceptLanguage()))
                )
                .build()

            IO.socket(socketEndpoint, options).also { currentSocket ->
                registerConnectionListeners(currentSocket)
                // Eski soket reauth/reconnect'te değişti; açık abonelikleri yeni sokete geri bağla.
                reattachEventListeners(currentSocket)
                currentSocket.connect()
            }
        }.getOrElse { error ->
            Log.e(TAG, "Socket init failed for $socketEndpoint: ${error.message}", error)
            connectionState.value = SocketConnectionState.ERROR
            null
        }

    private companion object {
        const val TAG = "SocketConnection"
        const val REAUTH_DEBOUNCE_MS = 10_000L
        const val ACCEPT_LANGUAGE_HEADER = "Accept-Language"
    }
}
