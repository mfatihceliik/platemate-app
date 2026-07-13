package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverForYouUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeCountUiModel

data class DiscoverHomeUiData(
    val metrics: List<DiscoverMetricUiModel>,
    val cityStats: List<DiscoverCityStatUiModel>,
    val recentActivities: List<DiscoverRecentActivityUiModel>,
    val topReportTypes: List<DiscoverReportTypeCountUiModel>,
    val isPremium: Boolean,
    val forYou: DiscoverForYouUiModel?,
    val tabs: DiscoveryTabs
)
