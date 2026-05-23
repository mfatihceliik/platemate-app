package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

interface SocketMessagingDataSource {
    suspend fun sendMessage(chatRoomId: Long, content: String)
    fun observeMessages(): Flow<ChatMessage>
}

