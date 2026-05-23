package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatRoomDto(
    @SerializedName("id") val id: Long,
    @SerializedName("roomName") val roomName: String?,
    @SerializedName("lastMessageAt") val lastMessageAt: String?,
    @SerializedName("lastMessageContent") val lastMessageContent: String?,
    @SerializedName("otherParticipantName") val otherParticipantName: String?,
    @SerializedName("group") val group: Boolean
)