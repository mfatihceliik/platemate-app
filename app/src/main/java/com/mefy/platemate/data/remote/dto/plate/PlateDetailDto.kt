package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto

data class PlateDetailDto(
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double,
    @SerializedName("reviewCount") val reviewCount: Long,
    @SerializedName("weeklySearchCount") val weeklySearchCount: Long,
    @SerializedName("todayReviewCount") val todayReviewCount: Long,
    @SerializedName("todayReportCount") val todayReportCount: Long,
    @SerializedName("todayWeightedReportScore") val todayWeightedReportScore: Double,
    @SerializedName("score") val score: Double,
    @SerializedName("lastActivityAt") val lastActivityAt: String?,
    @SerializedName("topReportTypes") val reportTypeDto: List<ReportTypeDto>
)