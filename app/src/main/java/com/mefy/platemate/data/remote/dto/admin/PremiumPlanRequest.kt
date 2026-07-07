package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

/** Body for updating a premium plan's pricing (period is fixed / not editable). */
data class PremiumPlanRequest(
    @SerializedName("titles") val titles: Map<String, String>,
    @SerializedName("descriptions") val descriptions: Map<String, String>?,
    @SerializedName("amount") val amount: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("discountPercent") val discountPercent: Int?,
    @SerializedName("sortOrder") val sortOrder: Int
)

data class UpdatePremiumActiveRequest(
    @SerializedName("active") val active: Boolean
)
