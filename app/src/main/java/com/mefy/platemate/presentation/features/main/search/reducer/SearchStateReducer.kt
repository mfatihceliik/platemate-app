package com.mefy.platemate.presentation.features.main.search.reducer

import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
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
        isSearchEnabled = isPlateValid && state.submitState !is UiActionState.Loading,
        submitState = if (state.submitState is UiActionState.Loading) {
            UiActionState.Idle
        } else {
            state.submitState
        },
        formMessage = null
    )

    fun onSearchLoading(state: SearchUiState): SearchUiState = state.copy(
        submitState = UiActionState.Loading,
        isSearchEnabled = false,
        formMessage = null
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
        submitState = UiActionState.Idle,
        formMessage = null
    )

    fun onSearchError(
        state: SearchUiState,
        message: UiText
    ): SearchUiState = state.copy(
        isSearchEnabled = state.isPlateValid,
        submitState = UiActionState.Error,
        formMessage = message
    )

    fun onRecentSearchesUpdated(
        state: SearchUiState,
        recentSearches: List<SearchRecentUiModel>
    ): SearchUiState = state.copy(recentSearches = recentSearches)
}
