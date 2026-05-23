package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.chat.ChatMessageDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.chat.ChatMessage
import javax.inject.Inject

class ChatMessageMapper @Inject constructor() : Mapper<ChatMessageDto, ChatMessage> {
    override fun map(input: ChatMessageDto): ChatMessage = ChatMessage(
        id = input.id,
        chatRoomId = null,
        senderUsername = input.senderUsername,
        content = input.messageContent,
        sentAt = input.sentAt.toAppDateTimeOrNull(),
        isRead = input.read
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}

