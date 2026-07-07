package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PremiumPlanAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("period") val period: String?,
    @SerializedName("titles") val titles: Map<String, String>?,
    @SerializedName("descriptions") val descriptions: Map<String, String>?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("discountPercent") val discountPercent: Int?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
