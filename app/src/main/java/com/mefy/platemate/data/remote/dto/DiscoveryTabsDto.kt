package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto

data class DiscoveryTabsDto(
    @SerializedName("trendPlates") val trendPlates: List<PlateDetailDto>?,
    @SerializedName("dangerousPlates") val dangerousPlates: List<PlateDetailDto>?,
    @SerializedName("goodDriverPlates") val goodDriverPlates: List<PlateDetailDto>?,
    @SerializedName("newPlates") val newPlates: List<PlateDetailDto>?
)
