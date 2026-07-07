package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel

@Immutable
data class DiscoverUiState(
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: UiText? = null,
    val selectedFilter: DiscoverFilterUi = DiscoverFilterUi.Trend,
    val metrics: List<DiscoverMetricUiModel> = emptyList(),
    val plateDetail: List<PlateDetailUiModel> = emptyList(),
    val cityStats: List<DiscoverCityStatUiModel> = emptyList(),
    val recentActivities: List<DiscoverRecentActivityUiModel> = emptyList()
)
