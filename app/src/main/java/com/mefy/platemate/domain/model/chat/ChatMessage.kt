package com.mefy.platemate.domain.model.chat

import com.mefy.platemate.domain.model.common.AppDateTime

data class ChatMessage(
    val id: Long,
    val chatRoomId: Long?,
    val senderUsername: String,
    val content: String,
    val sentAt: AppDateTime?,
    val isRead: Boolean
)
