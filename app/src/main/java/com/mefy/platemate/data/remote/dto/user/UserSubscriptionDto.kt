package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.subscription.SubscriptionStatusDto

data class UserSubscriptionDto(
    @SerializedName("id") val id: Long,
    @SerializedName("purchasedDays") val purchasedDays: Int,
    @SerializedName("status") val status: SubscriptionStatusDto,
    @SerializedName("startedAt") val startedAt: String,
    @SerializedName("expiresAt") val expiresAt: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)
