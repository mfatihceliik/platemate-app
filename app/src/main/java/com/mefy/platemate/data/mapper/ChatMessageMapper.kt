package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.chat.ChatMessageDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.MessageStatus
import javax.inject.Inject

class ChatMessageMapper @Inject constructor() : Mapper<ChatMessageDto, ChatMessage> {
    override fun map(input: ChatMessageDto): ChatMessage = ChatMessage(
        id = input.id,
        chatRoomId = input.chatRoomId,
        senderUserId = input.senderUserId,
        senderUsername = input.senderUsername,
        content = input.messageContent,
        sentAt = input.sentAt.toAppDateTimeOrNull(),
        isRead = input.read,
        status = MessageStatus.fromString(input.status),
        deliveredAt = input.deliveredAt.toAppDateTimeOrNull(),
        readAt = input.readAt.toAppDateTimeOrNull(),
        clientMessageId = input.clientMessageId,
        replyToMessageId = input.replyToMessageId,
        replyToSenderUsername = input.replyToSenderUsername,
        replyToContentPreview = input.replyToContentPreview
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}

