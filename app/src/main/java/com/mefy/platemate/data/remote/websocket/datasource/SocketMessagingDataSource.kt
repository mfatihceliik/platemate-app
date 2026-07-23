package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.MessageDeletedSignal
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.chat.UserPresence
import kotlinx.coroutines.flow.Flow

/** Result of an ack-aware send: the server either persisted it, rejected it, or never acked in time. */
sealed interface SendMessageAckResult {
    data class Success(val message: ChatMessage) : SendMessageAckResult
    data class ServerError(val message: String) : SendMessageAckResult
    data object TimedOut : SendMessageAckResult
}

interface SocketMessagingDataSource {
    suspend fun sendMessage(
        chatRoomId: Long,
        content: String,
        clientMessageId: String,
        replyToMessageId: Long? = null
    ): SendMessageAckResult
    suspend fun joinRoom(chatRoomId: Long)
    fun observeMessages(): Flow<ChatMessage>
    fun observeMessageDelivered(): Flow<MessageStatusSignal>
    fun observeMessageRead(): Flow<MessageStatusSignal>
    fun observeMessageDeleted(): Flow<MessageDeletedSignal>
    fun observeErrors(): Flow<String>
    fun observePresence(): Flow<UserPresence>
}

