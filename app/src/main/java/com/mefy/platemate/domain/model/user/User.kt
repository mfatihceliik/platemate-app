package com.mefy.platemate.domain.model.user

data class User(
    val id: Long,
    val username: String,
    val email: String?,
    val token: String?,
    val premiumUntil: String?,
    val premiumActive: Boolean,
    val roleCode: String?,
    val currentSubscriptionStartedAt: String?,
    val currentSubscriptionExpiresAt: String?,
    val currentSubscriptionPurchasedDays: Int?,
    val currentSubscriptionStatus: String?
)
