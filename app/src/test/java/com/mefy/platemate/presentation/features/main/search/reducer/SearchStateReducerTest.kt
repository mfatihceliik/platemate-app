package com.mefy.platemate.presentation.features.main.search.reducer

import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.search.SearchUiState
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchStateReducerTest {

    private val reducer = SearchStateReducer()

    @Test
    fun onPlateInputChanged_fromLoading_resetsSubmitStateAndEnablesSearch() {
        val start = SearchUiState(submitState = UiActionState.Loading)

        val state = reducer.onPlateInputChanged(
            state = start,
            formattedPlate = "34 ABC 123",
            isPlateValid = true,
            detectedCityName = "Istanbul"
        )

        assertEquals("34 ABC 123", state.plateInput)
        assertTrue(state.submitState is UiActionState.Idle)
        assertFalse(state.isSearchEnabled)
    }

    @Test
    fun onSearchSuccess_updatesInputAndSubmitState() {
        val state = reducer.onSearchSuccess(
            state = SearchUiState(),
            formattedPlate = "34 ABC 123",
            detectedCityName = "Istanbul"
        )

        assertEquals("34 ABC 123", state.plateInput)
        assertEquals("Istanbul", state.detectedCityName)
        assertTrue(state.submitState is UiActionState.Idle)
    }

    @Test
    fun onRecentSearchesUpdated_replacesRecentList() {
        val recent = listOf(
            SearchRecentUiModel(
                normalizedPlateCode = "34ABC123",
                plateCode = "34 ABC 123",
                cityName = "Istanbul",
                ratingAverage = 4.3,
                commentCount = 12L,
                reportTags = emptyList(),
                isBookmarked = false
            )
        )

        val state = reducer.onRecentSearchesUpdated(
            state = SearchUiState(isInitialLoading = true),
            recentSearches = recent
        )

        assertFalse(state.isInitialLoading)
        assertEquals(1, state.recentSearches.size)
        assertEquals("34ABC123", state.recentSearches.first().normalizedPlateCode)
    }

    @Test
    fun onInitialLoadFailed_turnsOffInitialLoading() {
        val state = reducer.onInitialLoadFailed(
            state = SearchUiState(isInitialLoading = true)
        )

        assertFalse(state.isInitialLoading)
    }

    @Test
    fun onSearchError_keepsValidityAndSetsInlineMessage() {
        val state = reducer.onSearchError(
            state = SearchUiState(isPlateValid = true),
            message = UiText.Dynamic("network")
        )

        assertTrue(state.isSearchEnabled)
        assertTrue(state.submitState is UiActionState.Error)
        assertEquals(UiText.Dynamic("network"), state.formMessage)
    }
}
