package com.mefy.platemate.data.remote.dto.premium

import com.google.gson.annotations.SerializedName

data class PremiumFeatureDto(
    @SerializedName("id") val id: Long,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
