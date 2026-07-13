package com.mefy.platemate.domain.model.discovery

data class DiscoveryExtendedStats(
    val yesterdaySearchCount: Long,
    val yesterdayReviewCount: Long,
    val yesterdayReportCount: Long,
    val searchDeltaPercent: Double,
    val reviewDeltaPercent: Double,
    val reportDeltaPercent: Double,
    val topReportTypesToday: List<ReportTypeCount>
)
