package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMyChatRooms(): AppResult<List<ChatRoom>>
    suspend fun createOrGetPrivateChatRoom(otherUserId: Long): AppResult<ChatRoom>
    suspend fun getRoomMessages(roomId: Long): AppResult<List<ChatMessage>>
    suspend fun markMessagesAsRead(roomId: Long): AppResult<Unit>
    suspend fun sendMessage(chatRoomId: Long, content: String): AppResult<Unit>
    fun observeMessages(roomId: Long): Flow<ChatMessage>
}

