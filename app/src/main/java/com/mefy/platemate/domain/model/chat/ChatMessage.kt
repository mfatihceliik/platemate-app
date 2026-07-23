package com.mefy.platemate.domain.model.chat

import com.mefy.platemate.domain.model.common.AppDateTime

data class ChatMessage(
    val id: Long,
    val chatRoomId: Long?,
    val senderUserId: Long?,
    val senderUsername: String,
    val content: String,
    val sentAt: AppDateTime?,
    val isRead: Boolean,
    val status: MessageStatus,
    val deliveredAt: AppDateTime?,
    val readAt: AppDateTime?,
    val clientMessageId: String? = null,
    val replyToMessageId: Long? = null,
    val replyToSenderUsername: String? = null,
    val replyToContentPreview: String? = null
)
