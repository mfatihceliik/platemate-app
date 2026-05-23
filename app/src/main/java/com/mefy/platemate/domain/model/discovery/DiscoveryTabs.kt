package com.mefy.platemate.domain.model.discovery

import com.mefy.platemate.domain.model.plate.PlateDetail

data class DiscoveryTabs(
    val trendPlates: List<PlateDetail>,
    val attentionPlates: List<PlateDetail>,
    val goodDriverPlates: List<PlateDetail>,
    val newPlates: List<PlateDetail>
)
