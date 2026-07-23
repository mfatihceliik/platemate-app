package com.mefy.platemate.data.remote.websocket.datasource

import android.util.Log
import com.mefy.platemate.BuildConfig
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.MessageDeletedSignal
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.domain.model.chat.MessageStatusSignal
import com.mefy.platemate.domain.model.chat.UserPresence
import com.mefy.platemate.domain.model.common.AppDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import org.json.JSONObject

@Singleton
class SocketMessagingDataSourceImpl @Inject constructor(
    private val connectionManager: SocketConnectionManager
) : SocketMessagingDataSource {

    override suspend fun sendMessage(
        chatRoomId: Long,
        content: String,
        clientMessageId: String,
        replyToMessageId: Long?
    ): SendMessageAckResult {
        val payload = JSONObject().apply {
            put("chatRoomId", chatRoomId)
            put("content", content)
            put("clientMessageId", clientMessageId)
            if (replyToMessageId != null) put("replyToMessageId", replyToMessageId)
        }
        val ack = connectionManager.emitWithAck(eventName = EVENT_SEND_MESSAGE, payload = payload)
            ?: return SendMessageAckResult.TimedOut

        // Reuses the {success, message, data} DataResult shape every REST endpoint already
        // returns — same envelope the socket ack now carries for success/business-error/exception.
        val success = ack.optBoolean("success", false)
        if (!success) {
            return SendMessageAckResult.ServerError(
                ack.optNullableString("message") ?: "Mesaj gönderilemedi"
            )
        }
        val message = ack.optJSONObject("data")?.toChatMessageOrNull()
            ?: return SendMessageAckResult.ServerError("Mesaj gönderilemedi")
        return SendMessageAckResult.Success(message)
    }

    // Backend join_room listener expects the raw roomId (Long), not a JSON object.
    override suspend fun joinRoom(chatRoomId: Long) {
        connectionManager.emit(eventName = EVENT_JOIN_ROOM, value = chatRoomId)
    }

    override fun observeMessages(): Flow<ChatMessage> =
        connectionManager.observeEvent(eventName = EVENT_NEW_MESSAGE)
            .mapNotNull { payload ->
                payload.toChatMessageOrNull().also {
                    if (BuildConfig.DEBUG) Log.d(TAG, "new_message parsed roomId=${it?.chatRoomId} ok=${it != null}")
                }
            }

    override fun observeMessageDelivered(): Flow<MessageStatusSignal> =
        connectionManager.observeEvent(eventName = EVENT_MESSAGE_DELIVERED)
            .mapNotNull { payload -> payload.toStatusSignalOrNull(MessageStatus.DELIVERED) }

    override fun observeMessageRead(): Flow<MessageStatusSignal> =
        connectionManager.observeEvent(eventName = EVENT_MESSAGE_READ)
            .mapNotNull { payload -> payload.toStatusSignalOrNull(MessageStatus.READ) }

    override fun observeMessageDeleted(): Flow<MessageDeletedSignal> =
        connectionManager.observeEvent(eventName = EVENT_MESSAGE_DELETED)
            .mapNotNull { payload -> payload.toMessageDeletedOrNull() }

    // Backend emits a Result/ErrorResult payload ({ message, success, ... }) on business-rule
    // failures (limit reached, messaging disabled, blocked, declined).
    override fun observeErrors(): Flow<String> =
        connectionManager.observeEvent(eventName = EVENT_ERROR)
            .mapNotNull { payload -> payload.optNullableString("message") }

    // Backend pushes presence_online / presence_offline ({ userId, online }) to chat partners
    // whenever a user connects or disconnects (already gated by reciprocal visibility).
    override fun observePresence(): Flow<UserPresence> = merge(
        connectionManager.observeEvent(eventName = EVENT_PRESENCE_ONLINE)
            .mapNotNull { payload -> payload.toPresenceOrNull(online = true) },
        connectionManager.observeEvent(eventName = EVENT_PRESENCE_OFFLINE)
            .mapNotNull { payload -> payload.toPresenceOrNull(online = false) }
    )

    private fun JSONObject.toPresenceOrNull(online: Boolean): UserPresence? = runCatching {
        val userId = optLongOrNull("userId") ?: return null
        UserPresence(userId = userId, online = online)
    }.getOrNull()

    private fun JSONObject.toChatMessageOrNull(): ChatMessage? = runCatching {
        ChatMessage(
            id = optLong("id"),
            chatRoomId = optLongOrNull("chatRoomId"),
            senderUserId = optLongOrNull("senderUserId"),
            senderUsername = optNullableString("senderUsername") ?: optNullableString("sender") ?: "",
            content = optNullableString("messageContent") ?: optNullableString("content") ?: "",
            sentAt = optNullableString("sentAt").toAppDateTimeOrNull(),
            isRead = optBoolean("read", false),
            status = MessageStatus.fromString(optNullableString("status")),
            deliveredAt = optNullableString("deliveredAt").toAppDateTimeOrNull(),
            readAt = optNullableString("readAt").toAppDateTimeOrNull(),
            clientMessageId = optNullableString("clientMessageId"),
            replyToMessageId = optLongOrNull("replyToMessageId"),
            replyToSenderUsername = optNullableString("replyToSenderUsername"),
            replyToContentPreview = optNullableString("replyToContentPreview")
        )
    }.getOrNull()

    private fun JSONObject.toMessageDeletedOrNull(): MessageDeletedSignal? = runCatching {
        val roomId = optLongOrNull("chatRoomId") ?: return null
        val messageId = optLongOrNull("messageId") ?: return null
        MessageDeletedSignal(roomId = roomId, messageId = messageId)
    }.getOrNull()

    private fun JSONObject.toStatusSignalOrNull(fallbackStatus: MessageStatus): MessageStatusSignal? = runCatching {
        val roomId = optLongOrNull("roomId") ?: return null
        MessageStatusSignal(
            roomId = roomId,
            messageId = optLongOrNull("messageId"),
            status = optNullableString("status")?.let(MessageStatus::fromString) ?: fallbackStatus
        )
    }.getOrNull()

    private fun JSONObject.optNullableString(name: String): String? =
        if (has(name) && !isNull(name)) optString(name) else null

    private fun JSONObject.optLongOrNull(name: String): Long? =
        if (has(name) && !isNull(name)) optLong(name) else null

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let(::AppDateTime)

    private companion object {
        const val TAG = "SocketMessaging"
        const val EVENT_SEND_MESSAGE = "send_message"
        const val EVENT_JOIN_ROOM = "join_room"
        const val EVENT_NEW_MESSAGE = "new_message"
        const val EVENT_MESSAGE_DELIVERED = "message_delivered"
        const val EVENT_MESSAGE_READ = "message_read"
        const val EVENT_MESSAGE_DELETED = "message_deleted"
        const val EVENT_ERROR = "error"
        const val EVENT_PRESENCE_ONLINE = "presence_online"
        const val EVENT_PRESENCE_OFFLINE = "presence_offline"
    }
}
