package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("chatRoomId") val chatRoomId: Long,
    @SerializedName("content") val content: String
)