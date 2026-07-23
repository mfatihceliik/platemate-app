package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.city.CityStatsDto
import com.mefy.platemate.data.remote.dto.city.TopCityPlateDto

data class DiscoveryHomeResponseDto(
    @SerializedName("dailyStats") val dailyStats: DailyStatsDto?,
    @SerializedName("tabs") val tabs: DiscoveryTabsDto?,
    @SerializedName("cityStats") val cityStats: List<CityStatsDto>?,
    @SerializedName("topCityPlates") val topCityPlates: List<TopCityPlateDto>?,
    @SerializedName("recentActivities") val recentActivities: List<RecentActivityDto>?,
    @SerializedName("feedType") val feedType: String? = null,
    @SerializedName("extendedStats") val extendedStats: DiscoveryExtendedStatsDto? = null,
    @SerializedName("forYou") val forYou: DiscoveryForYouDto? = null,
    @SerializedName("tabOptions") val tabOptions: List<DiscoveryTabOptionDto>? = null
)
