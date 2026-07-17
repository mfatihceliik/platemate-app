package com.mefy.platemate.domain.model.chat

import com.mefy.platemate.domain.model.common.AppDateTime

data class ChatRoom(
    val id: Long,
    val roomName: String?,
    val lastMessageAt: AppDateTime?,
    val lastMessageContent: String?,
    val otherParticipantName: String?,
    val otherParticipantId: Long?,
    val isGroup: Boolean,
    val requestStatus: ChatRoomRequestStatus,
    val initiatorId: Long?,
    val unreadCount: Int = 0,
    val lastMessageSenderId: Long? = null
)
