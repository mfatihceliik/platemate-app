package com.mefy.platemate.domain.model.chat

import com.mefy.platemate.domain.model.common.AppDateTime

data class ChatRoom(
    val id: Long,
    val roomName: String?,
    val lastMessageAt: AppDateTime?,
    val lastMessageContent: String?,
    val otherParticipantName: String?,
    val isGroup: Boolean
)
