package com.mefy.platemate.data.remote.dto.friend

import com.google.gson.annotations.SerializedName

data class FriendshipDto(
    @SerializedName("id") val id: Long,
    @SerializedName("friendUserId") val friendUserId: Long,
    @SerializedName("friendUsername") val friendUsername: String,
    @SerializedName("status") val status: String?, // Now using statusCode or statusLabel from backend?
    @SerializedName("statusCode") val statusCode: String?, 
    @SerializedName("statusLabel") val statusLabel: String?,
    @SerializedName("requestedAt") val requestedAt: String?,
    @SerializedName("respondedAt") val respondedAt: String?
)