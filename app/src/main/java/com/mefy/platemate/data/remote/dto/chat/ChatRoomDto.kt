package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatRoomDto(
    @SerializedName("id") val id: Long,
    @SerializedName("roomName") val roomName: String?,
    @SerializedName("lastMessageAt") val lastMessageAt: String?,
    @SerializedName("lastMessageContent") val lastMessageContent: String?,
    @SerializedName("otherParticipantName") val otherParticipantName: String?,
    @SerializedName("otherParticipantId") val otherParticipantId: Long?,
    @SerializedName("group") val group: Boolean,
    @SerializedName("requestStatus") val requestStatus: String?,
    @SerializedName("initiatorId") val initiatorId: Long?,
    @SerializedName("unreadCount") val unreadCount: Int = 0,
    @SerializedName("lastMessageSenderId") val lastMessageSenderId: Long?
)