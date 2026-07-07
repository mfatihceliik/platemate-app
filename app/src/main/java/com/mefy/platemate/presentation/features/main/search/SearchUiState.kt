package com.mefy.platemate.presentation.features.main.search

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel

@Immutable
data class SearchUiState(
    val isInitialLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val countryCode: String = "TR",
    val plateInput: String = "",
    val isPlateValid: Boolean = false,
    val detectedCityName: String? = null,
    val recentSearches: List<SearchRecentUiModel> = emptyList(),
    val bookmarkedPlates: List<SearchRecentUiModel> = emptyList(),
    val alarmPlates: List<SearchRecentUiModel> = emptyList(),
    val isSearchEnabled: Boolean = false,
    val isSearching: Boolean = false
)
