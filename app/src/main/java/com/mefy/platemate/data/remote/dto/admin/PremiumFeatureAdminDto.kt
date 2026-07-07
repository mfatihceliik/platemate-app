package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PremiumFeatureAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("titles") val titles: Map<String, String>?,
    @SerializedName("subtitles") val subtitles: Map<String, String>?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
