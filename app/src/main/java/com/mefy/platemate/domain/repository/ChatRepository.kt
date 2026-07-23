package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.model.chat.MessageDeletedSignal
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.chat.UserPresence
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMyChatRooms(): AppResult<List<ChatRoom>>
    suspend fun getRoom(roomId: Long): AppResult<ChatRoom>
    suspend fun createOrGetPrivateChatRoom(otherUserId: Long): AppResult<ChatRoom>
    suspend fun getRoomMessages(roomId: Long): AppResult<List<ChatMessage>>
    // Local-only cross-room content search (Messages list "search inside messages"); only finds
    // messages in rooms already synced locally at least once — see syncRoomMessages().
    suspend fun searchMessages(query: String, limit: Int = 30): List<ChatMessage>

    // Offline-first (Room = tek doğru kaynak): UI bu Flow'ları dinler; sync* metotları REST'ten
    // Room'u tazeler; cache* metotları socket deltasını Room'a yazar.
    fun observeChatRooms(): Flow<List<ChatRoom>>
    fun observeRoomMessages(roomId: Long): Flow<List<ChatMessage>>
    suspend fun syncChatRooms(): AppResult<Unit>
    suspend fun syncRoomMessages(roomId: Long): AppResult<Unit>
    suspend fun cacheIncomingMessage(message: ChatMessage)
    suspend fun cacheMessageStatus(signal: MessageStatusSignal)

    suspend fun markMessagesAsRead(roomId: Long): AppResult<Unit>
    suspend fun sendMessage(chatRoomId: Long, content: String, replyToMessageId: Long? = null): AppResult<Unit>
    // Resends a locally FAILED message row (same content/clientMessageId), flipping it back to
    // PENDING and re-running the same ack+timeout flow.
    suspend fun retryMessage(chatRoomId: Long, messageId: Long): AppResult<Unit>

    // Deletes [messageId]. A negative id (never reached the server — still PENDING/FAILED) is a
    // pure local Room delete, no network call; a real server id soft-deletes for everyone via REST.
    suspend fun deleteMessage(messageId: Long): AppResult<Unit>
    suspend fun cacheMessageDeletion(signal: MessageDeletedSignal)
    suspend fun reportMessage(messageId: Long, reason: String): AppResult<Unit>

    // Bir önceki oturumdan kalan taşlaşmış (DELETED) satırları bu odadan yerelde temizler.
    // Yalnızca taze bir konuşma oturumu başlarken (startRoomSession) bir kez çağrılır — canlı
    // oturum sırasında (ör. reconnect resync) çağrılmaz, aksi halde o an ekranda görünen
    // "silindi" balonu kaybolurdu.
    suspend fun purgeDeletedMessages(roomId: Long)

    // Cold-start sweep: re-runs the send flow for every PENDING row left behind by a process
    // death mid-send, across all rooms. Safe to call anytime (no-op if nothing is orphaned) —
    // the backend's clientMessageId idempotency guard means a message that actually reached the
    // server before the process died reconciles to its existing row instead of duplicating.
    suspend fun recoverOrphanedPendingMessages()
    suspend fun joinRoom(roomId: Long)
    suspend fun acceptChatRequest(roomId: Long): AppResult<Unit>
    suspend fun declineChatRequest(roomId: Long): AppResult<Unit>
    suspend fun leaveRoom(roomId: Long): AppResult<Unit>
    fun observeChatErrors(): Flow<String>
    suspend fun getRoomPresence(roomId: Long): AppResult<UserPresence>
    fun observePresence(userId: Long): Flow<Boolean>
}

