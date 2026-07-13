package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiscoveryExtendedStatsDto(
    @SerializedName("yesterdaySearchCount") val yesterdaySearchCount: Long?,
    @SerializedName("yesterdayReviewCount") val yesterdayReviewCount: Long?,
    @SerializedName("yesterdayReportCount") val yesterdayReportCount: Long?,
    @SerializedName("searchDeltaPercent") val searchDeltaPercent: Double?,
    @SerializedName("reviewDeltaPercent") val reviewDeltaPercent: Double?,
    @SerializedName("reportDeltaPercent") val reportDeltaPercent: Double?,
    @SerializedName("topReportTypesToday") val topReportTypesToday: List<DiscoveryReportTypeCountDto>?
)
