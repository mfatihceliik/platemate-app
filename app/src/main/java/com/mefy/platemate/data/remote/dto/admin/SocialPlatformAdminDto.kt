package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class SocialPlatformAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String?,
    @SerializedName("labels") val labels: Map<String, String>?,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("backgroundColorHex") val backgroundColorHex: String?,
    @SerializedName("iconTintColorHex") val iconTintColorHex: String?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
