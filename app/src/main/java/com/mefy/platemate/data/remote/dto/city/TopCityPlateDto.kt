package com.mefy.platemate.data.remote.dto.city

import com.google.gson.annotations.SerializedName

data class TopCityPlateDto(
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("todayReviewCount") val todayReviewCount: Long,
    @SerializedName("todayReportCount") val todayReportCount: Long,
    @SerializedName("lastActivityAt") val lastActivityAt: String?,
)