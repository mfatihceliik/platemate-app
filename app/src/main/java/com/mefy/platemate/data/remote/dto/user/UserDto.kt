package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.subscription.SubscriptionStatusDto

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String?,
    @SerializedName("token") val token: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("premiumUntil") val premiumUntil: String?,
    @SerializedName("premiumActive") val premiumActive: Boolean,
    @SerializedName("roleCode") val roleCode: UserRoleCode?,
    @SerializedName("currentSubscriptionStartedAt") val currentSubscriptionStartedAt: String?,
    @SerializedName("currentSubscriptionExpiresAt") val currentSubscriptionExpiresAt: String?,
    @SerializedName("currentSubscriptionPurchasedDays") val currentSubscriptionPurchasedDays: Int?,
    @SerializedName("currentSubscriptionStatus") val currentSubscriptionStatus: SubscriptionStatusDto?
)
