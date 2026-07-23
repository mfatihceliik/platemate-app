package com.mefy.platemate.data.repository

import android.util.Log
import com.mefy.platemate.BuildConfig
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.common.result.onSuccessSuspend
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.notification.ActiveConversationTracker
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.room.TempMessageIdGenerator
import com.mefy.platemate.data.local.room.dao.ChatMessageDao
import com.mefy.platemate.data.local.room.dao.ChatRoomDao
import com.mefy.platemate.data.local.room.entity.ChatMessageEntity
import com.mefy.platemate.data.local.room.entity.ChatRoomEntity
import com.mefy.platemate.data.mapper.ChatMessageMapper
import com.mefy.platemate.data.remote.dto.chat.AddChatMessageReportRequest
import com.mefy.platemate.data.mapper.ChatRoomMapper
import com.mefy.platemate.data.mapper.PresenceMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.websocket.datasource.SendMessageAckResult
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.data.remote.rest.service.ChatApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.model.chat.ChatRoomRequestStatus
import com.mefy.platemate.domain.model.chat.MessageDeletedSignal
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.chat.UserPresence
import com.mefy.platemate.domain.repository.ChatRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val socketMessagingDataSource: SocketMessagingDataSource,
    private val socketConnectionDataSource: SocketConnectionDataSource,
    private val chatRoomDao: ChatRoomDao,
    private val chatMessageDao: ChatMessageDao,
    private val sessionStore: SessionStore,
    private val chatRoomMapper: ChatRoomMapper,
    private val chatMessageMapper: ChatMessageMapper,
    private val presenceMapper: PresenceMapper,
    private val activeConversationTracker: ActiveConversationTracker,
    private val tempMessageIdGenerator: TempMessageIdGenerator,
    private val appDispatchers: AppDispatchers
) : ChatRepository {

    private val chatRoomsSyncMutex = Mutex()
    @Volatile
    private var lastChatRoomsSyncAtMs: Long = 0L

    override suspend fun getMyChatRooms(): AppResult<List<ChatRoom>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getMyChatRooms() }.map(chatRoomMapper::mapList)
        }

    override suspend fun getRoom(roomId: Long): AppResult<ChatRoom> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getRoom(roomId) }.map(chatRoomMapper::map)
        }

    override suspend fun createOrGetPrivateChatRoom(otherUserId: Long): AppResult<ChatRoom> =
        withContext(appDispatchers.io) {
            safeApiCall { api.createOrGetPrivateChatRoom(otherUserId) }.map(chatRoomMapper::map)
        }

    override suspend fun getRoomMessages(roomId: Long): AppResult<List<ChatMessage>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getRoomMessages(roomId) }.map(chatMessageMapper::mapList)
        }

    override suspend fun searchMessages(query: String, limit: Int): List<ChatMessage> =
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.mapNotNull { it?.userId }.first()
            chatMessageDao.search(userId, query, limit).map { it.toDomain() }
        }

    // Aktif (ekranda açık) odanın unread'i reaktif olarak 0'a bastırılır: kullanıcı o odayı izliyorsa
    // tanım gereği okundu sayılır → rozet anında kaybolur ve REST sync onu diriltemez.
    override fun observeChatRooms(): Flow<List<ChatRoom>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            combine(
                chatRoomDao.observeRooms(userId),
                activeConversationTracker.activeRoomIdFlow
            ) { rooms, activeRoomId ->
                rooms.map { entity ->
                    if (entity.roomId == activeRoomId) entity.copy(unreadCount = 0).toDomain()
                    else entity.toDomain()
                }
            }
        }.flowOn(appDispatchers.io)

    override fun observeRoomMessages(roomId: Long): Flow<List<ChatMessage>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            chatMessageDao.observeByRoom(userId, roomId).map { msgs -> msgs.map(ChatMessageEntity::toDomain) }
        }.flowOn(appDispatchers.io)

    override suspend fun syncChatRooms(): AppResult<Unit> =
        withContext(appDispatchers.io) {
            chatRoomsSyncMutex.withLock {
                val now = System.currentTimeMillis()
                if (now - lastChatRoomsSyncAtMs < CHAT_ROOMS_SYNC_MIN_INTERVAL_MS) {
                    return@withLock AppResult.Success(Unit)
                }
                val userId = sessionStore.session.mapNotNull { it?.userId }.first()
                getMyChatRooms().also { result ->
                    if (result is AppResult.Success) {
                        lastChatRoomsSyncAtMs = now
                        // Kullanıcının o an açık olduğu odanın unread'ini diriltme; izlediği oda okundu sayılır.
                        val activeRoomId = activeConversationTracker.activeRoomId
                        chatRoomDao.upsertAll(
                            result.data.map { room ->
                                val entity = room.toEntity(userId)
                                if (entity.roomId == activeRoomId) entity.copy(unreadCount = 0) else entity
                            }
                        )
                    }
                }.map { }
            }
        }

    override suspend fun syncRoomMessages(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.mapNotNull { it?.userId }.first()
            getRoomMessages(roomId).also { result ->
                if (result is AppResult.Success) {
                    chatMessageDao.upsertAll(result.data.map { it.toEntity(userId) })
                }
            }.map { }
        }

    override suspend fun cacheIncomingMessage(message: ChatMessage) {
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.mapNotNull { it?.userId }.first()
            chatMessageDao.upsert(message.toEntity(userId))
            val roomId = message.chatRoomId ?: run {
                if (BuildConfig.DEBUG) Log.w(TAG, "cacheIncomingMessage: null chatRoomId → msg upserted, room not touched")
                return@withContext
            }
            val affected = chatRoomDao.updatePreview(
                ownerUserId = userId,
                roomId = roomId,
                content = message.content,
                sentAt = message.sentAt?.iso8601,
                senderId = message.senderUserId
            )
            if (BuildConfig.DEBUG) Log.d(TAG, "cacheIncomingMessage room=$roomId updatePreview affected=$affected")
            if (affected == 0) {
                // Oda henüz yerelde yok (yeni konuşma) → REST'ten odayı çek; unreadCount de gelir.
                syncChatRooms()
            } else if (message.senderUserId != userId && activeConversationTracker.activeRoomId != roomId) {
                // Karşıdan gelen mesaj + kullanıcı o odada değil → okunmamış rozeti için sayacı artır.
                chatRoomDao.incrementUnread(userId, roomId)
            }
        }
    }

    override suspend fun cacheMessageStatus(signal: MessageStatusSignal) {
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.mapNotNull { it?.userId }.first()
            if (signal.messageId != null) {
                chatMessageDao.updateStatus(userId, signal.messageId, signal.status.name)
            } else {
                // Okundu sinyali: bu kullanıcının o odada GÖNDERDİĞİ mesajlar okundu işaretlenir.
                chatMessageDao.updateStatusForSender(userId, signal.roomId, userId, signal.status.name)
            }
        }
    }

    override suspend fun markMessagesAsRead(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            // Yerel okunmamış sayacını hemen sıfırla (iyimser) — API sonucundan bağımsız.
            sessionStore.session.mapNotNull { it?.userId }.first().let { chatRoomDao.clearUnread(it, roomId) }
            safeResultCall { api.markMessagesAsRead(roomId) }
        }

    // Send via socket so the backend persists AND broadcasts new_message to every OTHER
    // participant. The sender's own copy is written locally as an optimistic PENDING row
    // *before* the network call — this is what makes the message visible in the list the
    // instant the user hits send, instead of only after a round-trip — then reconciled to the
    // real server row (success) or flipped to FAILED in place (server error / 30s timeout),
    // never silently dropped.
    override suspend fun sendMessage(chatRoomId: Long, content: String, replyToMessageId: Long?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val session = sessionStore.session.first() ?: return@withContext AppResult.Error(AppError.Network())
            val clientMessageId = UUID.randomUUID().toString()
            val tempId = tempMessageIdGenerator.nextId()
            // İyimser önizleme: sunucu ack'i gelene kadar alıntı satırı yerelde zaten önbellekteki
            // hedef mesajdan türetilir; ack, otoriter değerlerle reconcilePending üzerinden üzerine yazar.
            val replyTo = replyToMessageId?.let { chatMessageDao.getByOwnerAndId(session.userId, it) }

            val pending = pendingEntity(session, tempId, chatRoomId, content, clientMessageId, replyTo)
            chatMessageDao.upsert(pending)
            // Konuşma listesindeki önizlemeyi de iyimser güncelle — yoksa mesaj kendi ekranında
            // anında görünse de Messages listesi bir sonraki (throttled) REST sync'e kadar bayat kalır.
            val affected = chatRoomDao.updatePreview(
                ownerUserId = session.userId,
                roomId = chatRoomId,
                content = pending.content,
                sentAt = pending.sentAt,
                senderId = session.userId
            )
            if (affected == 0) {
                // Oda henüz yerelde yok (yeni konuşmadaki ilk mesaj) → REST'ten odayı çek.
                syncChatRooms()
            }

            performSend(session.userId, tempId, chatRoomId, content, clientMessageId, replyToMessageId)
            AppResult.Success(Unit)
        }

    // Reruns the send flow for an existing FAILED row: same clientMessageId/content, flipped
    // back to PENDING first so the UI shows the clock icon again while the retry is in flight.
    override suspend fun retryMessage(chatRoomId: Long, messageId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val session = sessionStore.session.first() ?: return@withContext AppResult.Error(AppError.Network())
            val failed = chatMessageDao.getByOwnerAndId(session.userId, messageId)
                ?: return@withContext AppResult.Error(AppError.Network())
            val clientMessageId = failed.clientMessageId ?: UUID.randomUUID().toString()

            chatMessageDao.updateStatus(session.userId, messageId, MessageStatus.PENDING.name)
            performSend(session.userId, messageId, chatRoomId, failed.content, clientMessageId, failed.replyToMessageId)
            AppResult.Success(Unit)
        }

    // Runs one performSend() per orphaned row, all concurrently (not sequentially) — an
    // unreachable socket makes each one independently wait up to the 30s ack timeout, so
    // sequencing N of them would block recovery for up to N*30s instead of ~30s total.
    override suspend fun recoverOrphanedPendingMessages() {
        withContext(appDispatchers.io) {
            val ownerUserId = sessionStore.session.mapNotNull { it?.userId }.first()
            val orphaned = chatMessageDao.getAllPendingForOwner(ownerUserId)
            if (orphaned.isEmpty()) return@withContext

            coroutineScope {
                orphaned.forEach { row ->
                    val chatRoomId = row.chatRoomId
                    val clientMessageId = row.clientMessageId
                    if (chatRoomId == null || clientMessageId == null) {
                        // Missing a room or correlation id: can't safely resend or reconcile —
                        // surface it as FAILED so the user gets the manual-retry affordance
                        // instead of a permanently stuck clock icon.
                        launch { chatMessageDao.updateStatus(ownerUserId, row.messageId, MessageStatus.FAILED.name) }
                        return@forEach
                    }
                    launch {
                        performSend(ownerUserId, row.messageId, chatRoomId, row.content, clientMessageId, row.replyToMessageId)
                    }
                }
            }
        }
    }

    override suspend fun deleteMessage(messageId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val ownerUserId = sessionStore.session.mapNotNull { it?.userId }.first()
            if (messageId < 0) {
                // Temp id: never reached the server (still PENDING/FAILED) — nothing to delete
                // remotely, just drop the local row.
                chatMessageDao.deleteById(ownerUserId, messageId)
                AppResult.Success(Unit)
            } else {
                safeResultCall { api.deleteMessage(messageId) }.onSuccessSuspend {
                    chatMessageDao.tombstone(ownerUserId, messageId, MessageStatus.DELETED.name)
                }
            }
        }

    // Live "delete for everyone" push from the other participant (or this sender's other
    // sessions). Idempotent with the optimistic tombstone written by deleteMessage() above —
    // whichever arrives first wins, the other is a harmless duplicate write.
    override suspend fun cacheMessageDeletion(signal: MessageDeletedSignal) {
        withContext(appDispatchers.io) {
            val ownerUserId = sessionStore.session.mapNotNull { it?.userId }.first()
            chatMessageDao.tombstone(ownerUserId, signal.messageId, MessageStatus.DELETED.name)
        }
    }

    override suspend fun reportMessage(messageId: Long, reason: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.reportMessage(messageId, AddChatMessageReportRequest(reason)) }
        }

    override suspend fun purgeDeletedMessages(roomId: Long) {
        withContext(appDispatchers.io) {
            val ownerUserId = sessionStore.session.mapNotNull { it?.userId }.first()
            chatMessageDao.deleteTombstonedForRoom(ownerUserId, roomId)
        }
    }

    private fun pendingEntity(
        session: AuthSession,
        tempId: Long,
        chatRoomId: Long,
        content: String,
        clientMessageId: String,
        replyTo: ChatMessageEntity? = null
    ) = ChatMessageEntity(
        ownerUserId = session.userId,
        messageId = tempId,
        chatRoomId = chatRoomId,
        senderUserId = session.userId,
        senderUsername = session.username,
        content = content,
        // Naive-local, matching the server's own LocalDateTime.now() convention (no zone/UTC) —
        // sent_at is sorted/displayed as a raw string everywhere, so the optimistic row and the
        // eventual server-reconciled row must share the same clock convention or they skew apart
        // by the device's UTC offset, misordering the message in the list.
        sentAt = LocalDateTime.now().toString(),
        isRead = false,
        status = MessageStatus.PENDING.name,
        deliveredAt = null,
        readAt = null,
        clientMessageId = clientMessageId,
        replyToMessageId = replyTo?.messageId,
        replyToSenderUsername = replyTo?.senderUsername,
        replyToContentPreview = replyTo?.content
    )

    // Socket emit buffers silently while disconnected → the message would be lost with no
    // feedback, so connectivity is guarded first; everything past that point (server error,
    // dropped ack, 30s timeout) resolves the already-visible PENDING row to FAILED in place
    // rather than surfacing a one-shot error the caller has to relay.
    private suspend fun performSend(
        ownerUserId: Long,
        tempId: Long,
        chatRoomId: Long,
        content: String,
        clientMessageId: String,
        replyToMessageId: Long? = null
    ) {
        if (!socketConnectionDataSource.isConnected()) {
            socketConnectionDataSource.connect()
        }
        if (!socketConnectionDataSource.isConnected()) {
            chatMessageDao.updateStatus(ownerUserId, tempId, MessageStatus.FAILED.name)
            return
        }

        val result = runCatching {
            socketMessagingDataSource.sendMessage(chatRoomId, content, clientMessageId, replyToMessageId)
        }.getOrElse { SendMessageAckResult.ServerError(it.message ?: "Mesaj gönderilemedi") }

        when (result) {
            is SendMessageAckResult.Success -> {
                chatMessageDao.reconcilePending(ownerUserId, tempId, result.message.toEntity(ownerUserId))
                // Sunucu-otoriter değerlerle önizlemeyi tazele — retryMessage()/
                // recoverOrphanedPendingMessages() sendMessage()'ın iyimser adımından geçmez,
                // bu yüzden liste güncellemesi tek başına burada da yapılmalı.
                chatRoomDao.updatePreview(
                    ownerUserId = ownerUserId,
                    roomId = chatRoomId,
                    content = result.message.content,
                    sentAt = result.message.sentAt?.iso8601,
                    senderId = result.message.senderUserId
                )
            }
            is SendMessageAckResult.ServerError, SendMessageAckResult.TimedOut ->
                chatMessageDao.updateStatus(ownerUserId, tempId, MessageStatus.FAILED.name)
        }
    }

    // Join the socket room so the backend's room broadcast (new_message echo) reaches us.
    // Needed for freshly created rooms that did not exist at socket connect time.
    override suspend fun joinRoom(roomId: Long) {
        withContext(appDispatchers.io) {
            runCatching { socketMessagingDataSource.joinRoom(roomId) }
        }
    }

    override suspend fun acceptChatRequest(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.acceptChatRequest(roomId) }
        }

    override suspend fun declineChatRequest(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.declineChatRequest(roomId) }
        }

    override suspend fun leaveRoom(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.leaveRoom(roomId) }.onSuccessSuspend {
                // Server soft-hides the participant row; mirror that locally so the conversation
                // disappears from the Messages list immediately instead of only after logout.
                sessionStore.session.mapNotNull { it?.userId }.first().let { userId ->
                    chatRoomDao.deleteRoom(userId, roomId)
                    chatMessageDao.deleteMessagesForRoom(userId, roomId)
                }
            }
        }

    override fun observeChatErrors(): Flow<String> =
        socketMessagingDataSource.observeErrors()
            .flowOn(appDispatchers.io)

    override suspend fun getRoomPresence(roomId: Long): AppResult<UserPresence> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getRoomPresence(roomId) }.map(presenceMapper::map)
        }

    override fun observePresence(userId: Long): Flow<Boolean> =
        socketMessagingDataSource.observePresence()
            .filter { it.userId == userId }
            .map { it.online }
            .flowOn(appDispatchers.io)

    private fun ChatMessage.toEntity(ownerUserId: Long): ChatMessageEntity = ChatMessageEntity(
        ownerUserId = ownerUserId,
        messageId = id,
        chatRoomId = chatRoomId,
        senderUserId = senderUserId,
        senderUsername = senderUsername,
        content = content,
        sentAt = sentAt?.iso8601,
        isRead = isRead,
        status = status.name,
        deliveredAt = deliveredAt?.iso8601,
        readAt = readAt?.iso8601,
        clientMessageId = clientMessageId,
        replyToMessageId = replyToMessageId,
        replyToSenderUsername = replyToSenderUsername,
        replyToContentPreview = replyToContentPreview
    )

    private fun ChatRoom.toEntity(ownerUserId: Long): ChatRoomEntity = ChatRoomEntity(
        ownerUserId = ownerUserId,
        roomId = id,
        roomName = roomName,
        lastMessageAt = lastMessageAt?.iso8601,
        lastMessageContent = lastMessageContent,
        otherParticipantName = otherParticipantName,
        otherParticipantId = otherParticipantId,
        isGroup = isGroup,
        requestStatus = requestStatus.name,
        initiatorId = initiatorId,
        unreadCount = unreadCount,
        lastMessageSenderId = lastMessageSenderId
    )

    private companion object {
        const val TAG = "ChatRepository"
        // Foreground-donusu / soket-reconnect / Messages acilisi / FCM push gibi bagimsiz
        // tetikleyiciler ust uste binebiliyor; bu pencere icinde tekrar senkron atlanip
        // yerel (Room, socket'ten zaten canli guncellenen) veri kaynak olarak kullanilir.
        const val CHAT_ROOMS_SYNC_MIN_INTERVAL_MS = 10_000L
    }
}

private fun ChatMessageEntity.toDomain(): ChatMessage = ChatMessage(
    id = messageId,
    chatRoomId = chatRoomId,
    senderUserId = senderUserId,
    senderUsername = senderUsername,
    content = content,
    sentAt = sentAt?.let(::AppDateTime),
    isRead = isRead,
    status = MessageStatus.fromString(status),
    deliveredAt = deliveredAt?.let(::AppDateTime),
    readAt = readAt?.let(::AppDateTime),
    clientMessageId = clientMessageId,
    replyToMessageId = replyToMessageId,
    replyToSenderUsername = replyToSenderUsername,
    replyToContentPreview = replyToContentPreview
)

private fun ChatRoomEntity.toDomain(): ChatRoom = ChatRoom(
    id = roomId,
    roomName = roomName,
    lastMessageAt = lastMessageAt?.let(::AppDateTime),
    lastMessageContent = lastMessageContent,
    otherParticipantName = otherParticipantName,
    otherParticipantId = otherParticipantId,
    isGroup = isGroup,
    requestStatus = ChatRoomRequestStatus.fromString(requestStatus),
    initiatorId = initiatorId,
    unreadCount = unreadCount,
    lastMessageSenderId = lastMessageSenderId
)


