package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.chat.UserPresence
import kotlinx.coroutines.flow.Flow

interface SocketMessagingDataSource {
    suspend fun sendMessage(chatRoomId: Long, content: String)
    suspend fun joinRoom(chatRoomId: Long)
    fun observeMessages(): Flow<ChatMessage>
    fun observeMessageDelivered(): Flow<MessageStatusSignal>
    fun observeMessageRead(): Flow<MessageStatusSignal>
    fun observeErrors(): Flow<String>
    fun observePresence(): Flow<UserPresence>
}

