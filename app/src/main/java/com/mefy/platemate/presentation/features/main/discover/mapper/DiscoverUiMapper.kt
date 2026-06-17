package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.presentation.features.main.discover.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel

interface DiscoverUiMapper {
    fun mapHome(input: DiscoveryHome): DiscoverHomeUiData
    fun mapTabPlates(tabs: DiscoveryTabs, filter: DiscoverFilterUi, bookmarkedCodes: Set<String>): List<PlateDetailUiModel>
}
