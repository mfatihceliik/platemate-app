package com.mefy.platemate.domain.model.discovery

import com.mefy.platemate.domain.model.plate.PlateDetail

data class DiscoveryForYou(
    val followedPlates: List<PlateDetail>,
    val savedPlates: List<PlateDetail>,
    val followedPlateActivities: List<RecentActivity>,
    val premiumStats: DiscoveryPremiumStats?
)

data class DiscoveryPremiumStats(
    val weeklySearchCount: Long,
    val weeklyReviewCount: Long,
    val weeklyReportCount: Long,
    val weeklySearchDeltaPercent: Double,
    val weeklyReviewDeltaPercent: Double,
    val weeklyReportDeltaPercent: Double
)
