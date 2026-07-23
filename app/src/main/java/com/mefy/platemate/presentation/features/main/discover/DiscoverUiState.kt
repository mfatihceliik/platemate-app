package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverFeedFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverForYouUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeCountUiModel
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeOptionUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverTabOptionUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel

const val DEFAULT_TAB_CODE = "TREND"

@Immutable
data class DiscoverUiState(
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: UiText? = null,
    val selectedTabCode: String = DEFAULT_TAB_CODE,
    val tabOptions: List<DiscoverTabOptionUiModel> = DiscoverFilterUi.entries.map {
        DiscoverTabOptionUiModel(code = it.code, label = it.name)
    },
    val metrics: List<DiscoverMetricUiModel> = emptyList(),
    val plateDetail: List<PlateDetailUiModel> = emptyList(),
    val cityStats: List<DiscoverCityStatUiModel> = emptyList(),
    val recentActivities: List<DiscoverRecentActivityUiModel> = emptyList(),
    val topReportTypes: List<DiscoverReportTypeCountUiModel> = emptyList(),
    val activeFilters: DiscoverFeedFilterUi = DiscoverFeedFilterUi(),
    val filterDraft: DiscoverFeedFilterUi = DiscoverFeedFilterUi(),
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val page: Int = 0,
    val isPremium: Boolean = false,
    val forYou: DiscoverForYouUiModel? = null,
    val availableReportTypes: List<DiscoverReportTypeOptionUi> = emptyList(),
    val cardStyle: PMPlateCardStyle = PMPlateCardStyle.Classic
)
