package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("chatRoomId") val chatRoomId: Long,
    @SerializedName("content") val content: String,
    @SerializedName("clientMessageId") val clientMessageId: String? = null,
    @SerializedName("replyToMessageId") val replyToMessageId: Long? = null
)