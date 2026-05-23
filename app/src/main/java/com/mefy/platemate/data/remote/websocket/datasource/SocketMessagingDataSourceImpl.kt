package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.common.AppDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.json.JSONObject

@Singleton
class SocketMessagingDataSourceImpl @Inject constructor(
    private val connectionManager: SocketConnectionManager
) : SocketMessagingDataSource {

    override suspend fun sendMessage(chatRoomId: Long, content: String) {
        val payload = JSONObject().apply {
            put("chatRoomId", chatRoomId)
            put("content", content)
        }
        connectionManager.emit(eventName = "send_message", payload = payload)
    }

    override fun observeMessages(): Flow<ChatMessage> =
        connectionManager.observeEvent(eventName = "new_message")
            .mapNotNull { payload -> payload.toChatMessageOrNull() }

    private fun JSONObject.toChatMessageOrNull(): ChatMessage? = runCatching {
        ChatMessage(
            id = optLong("id"),
            chatRoomId = optLongOrNull("chatRoomId"),
            senderUsername = optNullableString("senderUsername") ?: optNullableString("sender") ?: "",
            content = optNullableString("messageContent") ?: optNullableString("content") ?: "",
            sentAt = optNullableString("sentAt").toAppDateTimeOrNull(),
            isRead = optBoolean("read", false)
        )
    }.getOrNull()

    private fun JSONObject.optNullableString(name: String): String? =
        if (has(name) && !isNull(name)) optString(name) else null

    private fun JSONObject.optLongOrNull(name: String): Long? =
        if (has(name) && !isNull(name)) optLong(name) else null

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let(::AppDateTime)
}


