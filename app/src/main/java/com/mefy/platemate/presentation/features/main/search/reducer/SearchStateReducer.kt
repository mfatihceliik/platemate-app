package com.mefy.platemate.presentation.features.main.search.reducer

import com.mefy.platemate.presentation.features.main.search.SearchUiState
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import javax.inject.Inject

class SearchStateReducer @Inject constructor() {

    fun onPlateInputChanged(
        state: SearchUiState,
        formattedPlate: String,
        isPlateValid: Boolean,
        detectedCityName: String?
    ): SearchUiState = state.copy(
        plateInput = formattedPlate,
        isPlateValid = isPlateValid,
        detectedCityName = detectedCityName,
        isSearchEnabled = isPlateValid && !state.isSearching,
        isSearching = false
    )

    fun onSearchLoading(state: SearchUiState): SearchUiState = state.copy(
        isSearching = true,
        isSearchEnabled = false
    )

    fun onSearchSuccess(
        state: SearchUiState,
        formattedPlate: String,
        detectedCityName: String?
    ): SearchUiState = state.copy(
        plateInput = formattedPlate,
        isPlateValid = true,
        detectedCityName = detectedCityName,
        isSearchEnabled = true,
        isSearching = false
    )

    fun onSearchError(state: SearchUiState): SearchUiState = state.copy(
        isSearchEnabled = state.isPlateValid,
        isSearching = false
    )

    fun onDataUpdated(
        state: SearchUiState,
        recentSearches: List<SearchRecentUiModel>,
        bookmarkedPlates: List<SearchRecentUiModel>
    ): SearchUiState = state.copy(
        recentSearches = recentSearches,
        bookmarkedPlates = bookmarkedPlates
    )
}
