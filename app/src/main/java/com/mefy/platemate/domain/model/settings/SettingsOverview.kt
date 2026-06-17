package com.mefy.platemate.domain.model.settings

import com.mefy.platemate.domain.model.profile.SocialMediaLink

data class SettingsOverview(
    val username: String?,
    val email: String?,
    val joinedAt: String?,
    val premiumActive: Boolean,
    val premiumUntil: String?,
    val userSettings: UserSettings,
    val socialMediaLinks: List<SocialMediaLink>
)
