package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel

interface DiscoverUiMapper {
    fun mapHome(input: DiscoveryHome, bookmarkedCodes: Set<String> = emptySet()): DiscoverHomeUiData
    fun mapFeedPlates(
        plates: List<PlateDetail>,
        filter: String,
        bookmarkedCodes: Set<String>,
        startRank: Int
    ): List<PlateDetailUiModel>
}
