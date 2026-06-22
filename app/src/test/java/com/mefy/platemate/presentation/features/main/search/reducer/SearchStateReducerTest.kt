package com.mefy.platemate.presentation.features.main.search.reducer

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
        val start = SearchUiState(isSearching = true)

        val state = reducer.onPlateInputChanged(
            state = start,
            formattedPlate = "34 ABC 123",
            isPlateValid = true,
            detectedCityName = "Istanbul"
        )

        assertEquals("34 ABC 123", state.plateInput)
        assertFalse(state.isSearching)
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
        assertFalse(state.isSearching)
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

        val state = reducer.onDataUpdated(
            state = SearchUiState(),
            recentSearches = recent,
            bookmarkedPlates = emptyList()
        )

        assertEquals(1, state.recentSearches.size)
        assertEquals("34ABC123", state.recentSearches.first().normalizedPlateCode)
    }

    @Test
    fun onSearchError_keepsValidityAndResetsSubmit() {
        val state = reducer.onSearchError(
            state = SearchUiState(isPlateValid = true)
        )

        assertTrue(state.isSearchEnabled)
        assertFalse(state.isSearching)
    }
}
