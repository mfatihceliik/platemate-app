package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("id") val id: Long,
    @SerializedName("senderUsername") val senderUsername: String,
    @SerializedName("messageContent") val messageContent: String,
    @SerializedName("sentAt") val sentAt: String,
    @SerializedName("read") val read: Boolean
)