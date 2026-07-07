package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/**
 * Shared body for adding and updating a social platform catalog entry.
 */
data class SocialPlatformRequest(
    @SerializedName("code") val code: String,
    @SerializedName("labels") val labels: Map<String, String>,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("backgroundColorHex") val backgroundColorHex: String?,
    @SerializedName("iconTintColorHex") val iconTintColorHex: String?,
    @SerializedName("sortOrder") val sortOrder: Int
)

data class UpdateSocialPlatformActiveRequest(
    @SerializedName("active") val active: Boolean
)
