package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.chat.UserPresence
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMyChatRooms(): AppResult<List<ChatRoom>>
    suspend fun getRoom(roomId: Long): AppResult<ChatRoom>
    suspend fun createOrGetPrivateChatRoom(otherUserId: Long): AppResult<ChatRoom>
    suspend fun getRoomMessages(roomId: Long): AppResult<List<ChatMessage>>

    // Offline-first (Room = tek doğru kaynak): UI bu Flow'ları dinler; sync* metotları REST'ten
    // Room'u tazeler; cache* metotları socket deltasını Room'a yazar.
    fun observeChatRooms(): Flow<List<ChatRoom>>
    fun observeRoomMessages(roomId: Long): Flow<List<ChatMessage>>
    suspend fun syncChatRooms(): AppResult<Unit>
    suspend fun syncRoomMessages(roomId: Long): AppResult<Unit>
    suspend fun cacheIncomingMessage(message: ChatMessage)
    suspend fun cacheMessageStatus(signal: MessageStatusSignal)

    suspend fun markMessagesAsRead(roomId: Long): AppResult<Unit>
    suspend fun sendMessage(chatRoomId: Long, content: String): AppResult<Unit>
    suspend fun joinRoom(roomId: Long)
    suspend fun acceptChatRequest(roomId: Long): AppResult<Unit>
    suspend fun declineChatRequest(roomId: Long): AppResult<Unit>
    suspend fun leaveRoom(roomId: Long): AppResult<Unit>
    fun observeChatErrors(): Flow<String>
    suspend fun getRoomPresence(roomId: Long): AppResult<UserPresence>
    fun observePresence(userId: Long): Flow<Boolean>
}

