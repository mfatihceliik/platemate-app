package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/** Shared body for adding and updating a premium feature (bilingual). */
data class PremiumFeatureRequest(
    @SerializedName("iconKey") val iconKey: String,
    @SerializedName("titles") val titles: Map<String, String>,
    @SerializedName("subtitles") val subtitles: Map<String, String>?,
    @SerializedName("sortOrder") val sortOrder: Int
)
