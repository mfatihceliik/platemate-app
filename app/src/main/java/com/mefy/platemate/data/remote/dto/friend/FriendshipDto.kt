package com.mefy.platemate.data.remote.dto.friend

import com.google.gson.annotations.SerializedName

data class FriendshipDto(
    @SerializedName("id") val id: Long,
    @SerializedName("friendUserId") val friendUserId: Long,
    @SerializedName("friendUsername") val friendUsername: String,
    @SerializedName("status") val status: String, // PENDING, ACCEPTED, REJECTED
    @SerializedName("createdAt") val createdAt: String
)