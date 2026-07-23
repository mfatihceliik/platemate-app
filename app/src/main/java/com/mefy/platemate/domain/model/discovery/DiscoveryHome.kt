package com.mefy.platemate.domain.model.discovery

data class DiscoveryHome(
    val dailyStats: DailyStats,
    val tabs: DiscoveryTabs,
    val cityStats: List<CityStats>,
    val topCityPlates: List<TopCityPlate>,
    val recentActivities: List<RecentActivity>,
    val feedType: DiscoveryFeedType = DiscoveryFeedType.FREE,
    val extendedStats: DiscoveryExtendedStats? = null,
    val forYou: DiscoveryForYou? = null,
    val tabOptions: List<DiscoveryTabOption> = emptyList()
)
