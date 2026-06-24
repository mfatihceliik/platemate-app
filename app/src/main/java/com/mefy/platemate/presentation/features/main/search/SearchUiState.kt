package com.mefy.platemate.presentation.features.main.search

import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel

data class SearchUiState(
    val errorMessage: UiText? = null,
    val countryCode: String = "TR",
    val plateInput: String = "",
    val isPlateValid: Boolean = false,
    val detectedCityName: String? = null,
    val recentSearches: List<SearchRecentUiModel> = emptyList(),
    val bookmarkedPlates: List<SearchRecentUiModel> = emptyList(),
    val isSearchEnabled: Boolean = false,
    val isSearching: Boolean = false
)
