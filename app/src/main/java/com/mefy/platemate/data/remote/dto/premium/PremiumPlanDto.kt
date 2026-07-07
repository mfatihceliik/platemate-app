package com.mefy.platemate.data.remote.dto.premium

import com.google.gson.annotations.SerializedName

data class PremiumPlanDto(
    @SerializedName("id") val id: Long,
    @SerializedName("period") val period: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("discountPercent") val discountPercent: Int?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
