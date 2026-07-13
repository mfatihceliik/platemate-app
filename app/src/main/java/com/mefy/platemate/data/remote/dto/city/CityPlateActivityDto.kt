package com.mefy.platemate.data.remote.dto.city

import com.google.gson.annotations.SerializedName

data class CityPlateActivityDto(
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("todayReviewCount") val todayReviewCount: Long?,
    @SerializedName("todayReportCount") val todayReportCount: Long?,
    @SerializedName("lastActivityAt") val lastActivityAt: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double?,
    @SerializedName("reviewCount") val reviewCount: Long?
)
