package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("id") val id: Long,
    @SerializedName("chatRoomId") val chatRoomId: Long?,
    @SerializedName("senderUserId") val senderUserId: Long?,
    @SerializedName("senderUsername") val senderUsername: String,
    @SerializedName("messageContent") val messageContent: String,
    @SerializedName("sentAt") val sentAt: String,
    @SerializedName("read") val read: Boolean,
    @SerializedName("status") val status: String?,
    @SerializedName("deliveredAt") val deliveredAt: String?,
    @SerializedName("readAt") val readAt: String?,
    @SerializedName("clientMessageId") val clientMessageId: String? = null,
    @SerializedName("replyToMessageId") val replyToMessageId: Long? = null,
    @SerializedName("replyToSenderUsername") val replyToSenderUsername: String? = null,
    @SerializedName("replyToContentPreview") val replyToContentPreview: String? = null
)