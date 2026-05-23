package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverRecentActivityUiModel

data class DiscoverHomeUiData(
    val metrics: List<DiscoverMetricUiModel>,
    val cityStats: List<DiscoverCityStatUiModel>,
    val recentActivities: List<DiscoverRecentActivityUiModel>,
    val tabs: DiscoveryTabs
)
