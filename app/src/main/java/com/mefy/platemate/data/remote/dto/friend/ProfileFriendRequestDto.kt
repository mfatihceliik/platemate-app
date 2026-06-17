package com.mefy.platemate.data.remote.dto.friend

import com.google.gson.annotations.SerializedName

data class ProfileFriendRequestDto(
    @SerializedName("id") val id: Long,
    @SerializedName("requesterUserId") val requesterUserId: Long,
    @SerializedName("requesterUsername") val requesterUsername: String,
    @SerializedName("addresseeUserId") val addresseeUserId: Long,
    @SerializedName("addresseeUsername") val addresseeUsername: String,
    @SerializedName("statusId") val statusId: Long?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("respondedAt") val respondedAt: String?,
    @SerializedName("lastActionAt") val lastActionAt: String?
)
