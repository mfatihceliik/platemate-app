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
import com.mefy.platemate.data.local.room.dao.ChatMessageDao
import com.mefy.platemate.data.local.room.dao.ChatRoomDao
import com.mefy.platemate.data.local.room.entity.ChatMessageEntity
import com.mefy.platemate.data.local.room.entity.ChatRoomEntity
import com.mefy.platemate.data.mapper.ChatMessageMapper
import com.mefy.platemate.data.mapper.ChatRoomMapper
import com.mefy.platemate.data.mapper.PresenceMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.data.remote.rest.service.ChatApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.model.chat.ChatRoomRequestStatus
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.chat.UserPresence
import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    private val appDispatchers: AppDispatchers
) : ChatRepository {

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
            val userId = sessionStore.session.first()?.userId
            getMyChatRooms().also { result ->
                if (result is AppResult.Success && userId != null) {
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

    override suspend fun syncRoomMessages(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.first()?.userId
            getRoomMessages(roomId).also { result ->
                if (result is AppResult.Success && userId != null) {
                    chatMessageDao.upsertAll(result.data.map { it.toEntity(userId) })
                }
            }.map { }
        }

    override suspend fun cacheIncomingMessage(message: ChatMessage) {
        withContext(appDispatchers.io) {
            val userId = sessionStore.session.first()?.userId ?: run {
                if (BuildConfig.DEBUG) Log.w(TAG, "cacheIncomingMessage: no session userId → dropped")
                return@withContext
            }
            chatMessageDao.upsert(message.toEntity(userId))
            val roomId = message.chatRoomId ?: run {
                if (BuildConfig.DEBUG) Log.w(TAG, "cacheIncomingMessage: null chatRoomId → msg upserted, room not touched")
                return@withContext
            }
            val affected = chatRoomDao.updatePreview(userId, roomId, message.content, message.sentAt?.iso8601)
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
            val userId = sessionStore.session.first()?.userId ?: return@withContext
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
            sessionStore.session.first()?.userId?.let { chatRoomDao.clearUnread(it, roomId) }
            safeResultCall { api.markMessagesAsRead(roomId) }
        }

    // Send via socket so the backend persists AND broadcasts new_message to the room
    // (including an echo to the sender). REST send remains only as an offline fallback.
    override suspend fun sendMessage(chatRoomId: Long, content: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            // Socket emit buffers silently while disconnected → the message would be lost with
            // no feedback. Guard on connectivity so a dead socket surfaces as an error the UI
            // can show, instead of "mesaj gitmiyor gibi" silence.
            if (!socketConnectionDataSource.isConnected()) {
                socketConnectionDataSource.connect()
            }
            if (!socketConnectionDataSource.isConnected()) {
                return@withContext AppResult.Error(AppError.Network())
            }
            runCatching { socketMessagingDataSource.sendMessage(chatRoomId, content) }
                .fold(
                    onSuccess = { AppResult.Success(Unit) },
                    onFailure = { AppResult.Error(AppError.Network(cause = it)) }
                )
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
                sessionStore.session.first()?.userId?.let { userId ->
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
        readAt = readAt?.iso8601
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
        unreadCount = unreadCount
    )

    private companion object {
        const val TAG = "ChatRepository"
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
    readAt = readAt?.let(::AppDateTime)
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
    unreadCount = unreadCount
)


