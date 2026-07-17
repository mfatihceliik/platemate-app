package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.chat.ChatRoomDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.model.chat.ChatRoomRequestStatus
import javax.inject.Inject

class ChatRoomMapper @Inject constructor() : Mapper<ChatRoomDto, ChatRoom> {
    override fun map(input: ChatRoomDto): ChatRoom = ChatRoom(
        id = input.id,
        roomName = input.roomName,
        lastMessageAt = input.lastMessageAt.toAppDateTimeOrNull(),
        lastMessageContent = input.lastMessageContent,
        otherParticipantName = input.otherParticipantName,
        otherParticipantId = input.otherParticipantId,
        isGroup = input.group,
        requestStatus = ChatRoomRequestStatus.fromString(input.requestStatus),
        initiatorId = input.initiatorId,
        unreadCount = input.unreadCount,
        lastMessageSenderId = input.lastMessageSenderId
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}

