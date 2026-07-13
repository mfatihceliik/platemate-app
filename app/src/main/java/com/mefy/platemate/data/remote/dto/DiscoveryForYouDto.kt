package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto

data class DiscoveryForYouDto(
    @SerializedName("followedPlates") val followedPlates: List<PlateDetailDto>?,
    @SerializedName("savedPlates") val savedPlates: List<PlateDetailDto>?,
    @SerializedName("followedPlateActivities") val followedPlateActivities: List<RecentActivityDto>?,
    @SerializedName("premiumStats") val premiumStats: DiscoveryPremiumStatsDto?
)
