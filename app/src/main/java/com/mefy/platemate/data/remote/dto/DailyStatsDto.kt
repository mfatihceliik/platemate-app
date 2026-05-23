package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DailyStatsDto(
    @SerializedName("todaySearchCount") val todaySearchCount: Long,
    @SerializedName("todayReviewCount") val todayReviewCount: Long,
    @SerializedName("todayReportCount") val todayReportCount: Long
)
