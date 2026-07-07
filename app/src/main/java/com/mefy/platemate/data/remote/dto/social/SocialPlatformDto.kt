package com.mefy.platemate.data.remote.dto.social

import com.google.gson.annotations.SerializedName

data class SocialPlatformDto(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("baseUrl") val baseUrl: String? = null,
    @SerializedName("backgroundColorHex") val backgroundColorHex: String?,
    @SerializedName("iconTintColorHex") val iconTintColorHex: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
