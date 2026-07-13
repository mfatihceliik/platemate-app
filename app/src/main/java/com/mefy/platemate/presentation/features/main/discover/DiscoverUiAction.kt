package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi

sealed interface DiscoverUiAction {
    data class FilterSelected(val filter: DiscoverFilterUi) : DiscoverUiAction
    data class TrendPlateClicked(val trendId: String) : DiscoverUiAction
    data class TrendPlateBookmarkClicked(val trendId: String) : DiscoverUiAction
    data class CityStatClicked(val cityId: Int, val cityName: String) : DiscoverUiAction
    data class RecentActivityPlateClicked(val plateCode: String) : DiscoverUiAction

    /** Filtre ekrani acilirken taslak aktif filtrelerden tohumlanir; premium ise rapor tipleri yuklenir. */
    data object FilterScreenOpened : DiscoverUiAction
    data class DraftCitiesChanged(val cityIds: List<Int>, val cityNames: List<String>) : DiscoverUiAction
    data class DraftMinRatingChanged(val minRating: Int) : DiscoverUiAction
    data class DraftReportTypeChanged(val code: String?, val label: String?) : DiscoverUiAction
    data class DraftWindowChanged(val windowDays: Int?) : DiscoverUiAction

    /** Taslak filtreyi aktif filtre olarak uygular ve feed'i ilk sayfadan yeniler. */
    data object FiltersApplied : DiscoverUiAction
    data object FiltersCleared : DiscoverUiAction

    data object LoadMoreRequested : DiscoverUiAction
    data object RefreshRequested : DiscoverUiAction
    data object RetryClicked : DiscoverUiAction
}
