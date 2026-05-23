package com.mefy.platemate.domain.model.discovery

data class DiscoveryHome(
    val dailyStats: DailyStats,
    val tabs: DiscoveryTabs,
    val cityStats: List<CityStats>,
    val topCityPlates: List<TopCityPlate>,
    val recentActivities: List<RecentActivity>
)
