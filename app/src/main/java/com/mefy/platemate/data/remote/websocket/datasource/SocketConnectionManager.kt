package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.model.socket.SocketConnectionState
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONObject

@Singleton
class SocketConnectionManager @Inject constructor(
    @param:Named("WebSocketEndpoint") private val socketEndpoint: String,
    private val sessionStore: SessionStore,
    private val appDispatchers: AppDispatchers
) {

    private var socket: Socket? = null
    private val connectionMutex = Mutex()
    private val connectionState = MutableStateFlow(SocketConnectionState.DISCONNECTED)

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

    fun observeEvent(eventName: String): Flow<JSONObject> = callbackFlow {
        val currentSocket = withContext(appDispatchers.io) { ensureConnected() }
        if (currentSocket == null) {
            close()
            return@callbackFlow
        }

        val listener = Emitter.Listener { args ->
            args.firstOrNull()
                ?.toJsonObjectOrNull()
                ?.let { trySend(it) }
        }

        currentSocket.on(eventName, listener)
        awaitClose { currentSocket.off(eventName, listener) }
    }

    private fun registerConnectionListeners(currentSocket: Socket) {
        currentSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            connectionState.value = SocketConnectionState.CONNECTED
        })
        currentSocket.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
            connectionState.value = SocketConnectionState.DISCONNECTED
        })
        currentSocket.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener {
            connectionState.value = SocketConnectionState.ERROR
        })
    }

    private fun Any.toJsonObjectOrNull(): JSONObject? = when (this) {
        is JSONObject -> this
        is String -> runCatching { JSONObject(this) }.getOrNull()
        else -> null
    }

    private suspend fun ensureConnected(): Socket? =
        connectionMutex.withLock {
            val existingSocket = socket
            if (existingSocket != null) {
                return@withLock existingSocket
            }

            val token = sessionStore.getToken()
            if (token.isNullOrBlank()) {
                connectionState.value = SocketConnectionState.UNAUTHORIZED
                return@withLock null
            }

            connectionState.value = SocketConnectionState.CONNECTING
            val connectedSocket = runCatching {
                val options = IO.Options.builder()
                    .setQuery("token=$token")
                    .build()

                IO.socket(socketEndpoint, options).also { currentSocket ->
                    registerConnectionListeners(currentSocket)
                    currentSocket.connect()
                }
            }.getOrElse {
                socket?.off()
                socket?.disconnect()
                socket = null
                connectionState.value = SocketConnectionState.ERROR
                return@withLock null
            }

            socket = connectedSocket
            connectedSocket
        }
}


