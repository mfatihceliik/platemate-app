package com.mefy.platemate.data.remote.dto.settings

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.social.SocialMediaLinkDto
import com.mefy.platemate.data.remote.dto.user.UserSettingsDto

data class SettingsOverviewDto(
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("joinedAt") val joinedAt: String? = null,
    @SerializedName("premiumActive") val premiumActive: Boolean = false,
    @SerializedName("premiumUntil") val premiumUntil: String? = null,
    @SerializedName("userSettings") val userSettings: UserSettingsDto,
    @SerializedName("socialMediaLinks") val socialMediaLinks: List<SocialMediaLinkDto> = emptyList()
)
