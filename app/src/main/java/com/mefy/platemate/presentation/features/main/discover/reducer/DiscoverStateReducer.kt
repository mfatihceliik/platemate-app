package com.mefy.platemate.presentation.features.main.discover.reducer

import com.mefy.platemate.presentation.features.main.discover.DiscoverFilterUi
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiState
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverHomeUiData
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import javax.inject.Inject

class DiscoverStateReducer @Inject constructor() {

    fun onInitialLoading(state: DiscoverUiState): DiscoverUiState =
        state.copy(isInitialLoading = true, isRefreshing = false)

    fun onRefreshing(state: DiscoverUiState): DiscoverUiState =
        state.copy(isRefreshing = true)

    fun onLoadError(state: DiscoverUiState): DiscoverUiState =
        state.copy(isInitialLoading = false, isRefreshing = false)

    fun onContentLoaded(
        state: DiscoverUiState,
        mappedHome: DiscoverHomeUiData,
        plateDetails: List<PlateDetailUiModel>
    ): DiscoverUiState = state.copy(
        isInitialLoading = false,
        isRefreshing = false,
        metrics = mappedHome.metrics,
        cityStats = mappedHome.cityStats,
        recentActivities = mappedHome.recentActivities,
        plateDetail = plateDetails
    )

    fun onFilterSelected(
        state: DiscoverUiState,
        filter: DiscoverFilterUi,
        plateDetails: List<PlateDetailUiModel>
    ): DiscoverUiState = state.copy(
        selectedFilter = filter,
        plateDetail = plateDetails
    )
}
