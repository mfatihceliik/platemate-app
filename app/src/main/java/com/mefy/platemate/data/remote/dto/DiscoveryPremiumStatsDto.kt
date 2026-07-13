package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiscoveryPremiumStatsDto(
    @SerializedName("weeklySearchCount") val weeklySearchCount: Long?,
    @SerializedName("weeklyReviewCount") val weeklyReviewCount: Long?,
    @SerializedName("weeklyReportCount") val weeklyReportCount: Long?,
    @SerializedName("weeklySearchDeltaPercent") val weeklySearchDeltaPercent: Double?,
    @SerializedName("weeklyReviewDeltaPercent") val weeklyReviewDeltaPercent: Double?,
    @SerializedName("weeklyReportDeltaPercent") val weeklyReportDeltaPercent: Double?
)
