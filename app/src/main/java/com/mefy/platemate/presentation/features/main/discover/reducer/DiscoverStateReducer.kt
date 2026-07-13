package com.mefy.platemate.presentation.features.main.discover.reducer

import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.features.uimodel.DiscoverFeedFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverForYouUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeOptionUi
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiState
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverHomeUiData
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import javax.inject.Inject

class DiscoverStateReducer @Inject constructor() {

    fun onInitialLoading(state: DiscoverUiState): DiscoverUiState =
        state.copy(isInitialLoading = true, isRefreshing = false, errorMessage = null)

    fun onRefreshing(state: DiscoverUiState): DiscoverUiState =
        state.copy(isRefreshing = true)

    /** İlk yükleme hatası ekran-içi gösterilir. */
    fun onLoadError(state: DiscoverUiState, message: UiText): DiscoverUiState =
        state.copy(isInitialLoading = false, isRefreshing = false, errorMessage = message)

    fun onContentLoaded(
        state: DiscoverUiState,
        mappedHome: DiscoverHomeUiData,
        plateDetails: List<PlateDetailUiModel>,
        endReached: Boolean
    ): DiscoverUiState = state.copy(
        isInitialLoading = false,
        isRefreshing = false,
        errorMessage = null,
        metrics = mappedHome.metrics,
        cityStats = mappedHome.cityStats,
        recentActivities = mappedHome.recentActivities,
        topReportTypes = mappedHome.topReportTypes,
        forYou = mappedHome.forYou,
        plateDetail = plateDetails,
        page = 0,
        endReached = endReached,
        isLoadingMore = false
    )

    fun onFilterSelected(
        state: DiscoverUiState,
        filter: DiscoverFilterUi,
        plateDetails: List<PlateDetailUiModel>,
        endReached: Boolean
    ): DiscoverUiState = state.copy(
        selectedFilter = filter,
        plateDetail = plateDetails,
        page = 0,
        endReached = endReached,
        isLoadingMore = false
    )

    /** Filtre ekrani acilisinda taslak, o an uygulanan filtrelerden tohumlanir. */
    fun onFilterDraftSeeded(state: DiscoverUiState): DiscoverUiState =
        state.copy(filterDraft = state.activeFilters)

    fun onFilterDraftChanged(state: DiscoverUiState, draft: DiscoverFeedFilterUi): DiscoverUiState =
        state.copy(filterDraft = draft)

    fun onFiltersApplied(state: DiscoverUiState, filters: DiscoverFeedFilterUi): DiscoverUiState =
        state.copy(
            activeFilters = filters,
            filterDraft = filters,
            plateDetail = emptyList(),
            page = 0,
            endReached = false,
            isLoadingMore = true
        )

    fun onLoadMoreStarted(state: DiscoverUiState): DiscoverUiState =
        state.copy(isLoadingMore = true)

    fun onPageLoaded(
        state: DiscoverUiState,
        plateDetails: List<PlateDetailUiModel>,
        page: Int,
        hasNext: Boolean
    ): DiscoverUiState = state.copy(
        plateDetail = plateDetails,
        page = page,
        endReached = !hasNext,
        isLoadingMore = false
    )

    fun onLoadMoreFailed(state: DiscoverUiState): DiscoverUiState =
        state.copy(isLoadingMore = false)

    fun onBookmarksChanged(
        state: DiscoverUiState,
        plateDetails: List<PlateDetailUiModel>,
        forYou: DiscoverForYouUiModel? = state.forYou
    ): DiscoverUiState = state.copy(plateDetail = plateDetails, forYou = forYou)

    fun onReportTypeOptionsLoaded(
        state: DiscoverUiState,
        options: List<DiscoverReportTypeOptionUi>
    ): DiscoverUiState = state.copy(availableReportTypes = options)

    fun onCardStyleChanged(
        state: DiscoverUiState,
        cardStyle: PMPlateCardStyle
    ): DiscoverUiState = state.copy(cardStyle = cardStyle)

    /** Premium durumu, feed yaniti degil canli oturum (AuthSession.role) uzerinden belirlenir. */
    fun onPremiumChanged(
        state: DiscoverUiState,
        isPremium: Boolean
    ): DiscoverUiState = state.copy(isPremium = isPremium)
}
