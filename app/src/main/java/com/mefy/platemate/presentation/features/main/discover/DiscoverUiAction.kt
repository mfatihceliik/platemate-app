package com.mefy.platemate.presentation.features.main.discover

sealed interface DiscoverUiAction {
    data class FilterSelected(val filter: DiscoverFilterUi) : DiscoverUiAction
    data class TrendPlateClicked(val trendId: String) : DiscoverUiAction
    data class TrendPlateBookmarkClicked(val trendId: String) : DiscoverUiAction
    data object RefreshRequested : DiscoverUiAction
    data object RetryClicked : DiscoverUiAction
}
