package com.mefy.platemate.presentation.features.main.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun searchScreen_displaysCoreSections() {
        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                SearchScreen(
                    state = SearchUiState(),
                    onAction = {},
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.search_header_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.search_recent_title)).assertIsDisplayed()
    }

    @Test
    fun bookmarkClick_dispatchesRecentBookmarkClickedAction() {
        var lastAction: SearchUiAction? = null

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                SearchScreen(
                    state = SearchUiState(
                        recentSearches = listOf(sampleRecentSearch(isBookmarked = false))
                    ),
                    onAction = { action -> lastAction = action },
                )
            }
        }

        composeRule.onNodeWithTag("recent_bookmark_34ABC123").performClick()

        assertEquals(
            SearchUiAction.RecentBookmarkClicked(normalizedPlateCode = "34ABC123"),
            lastAction
        )
    }

    @Test
    fun closeClick_dispatchesRecentDismissClickedAction() {
        var lastAction: SearchUiAction? = null

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                SearchScreen(
                    state = SearchUiState(
                        recentSearches = listOf(sampleRecentSearch(isBookmarked = false))
                    ),
                    onAction = { action -> lastAction = action },
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(getString(R.string.search_recent_remove))
            .performClick()

        assertEquals(
            SearchUiAction.RecentDismissClicked(normalizedPlateCode = "34ABC123"),
            lastAction
        )
    }

    private fun sampleRecentSearch(isBookmarked: Boolean): SearchRecentUiModel = SearchRecentUiModel(
        normalizedPlateCode = "34ABC123",
        plateCode = "34 ABC 123",
        cityName = "Istanbul",
        reportTags = listOf(
            PlateReportTagUiModel(
                code = "SAFE",
                label = "Safe",
                severity = "LOW",
                colorHex = "#00AA00"
            )
        ),
        ratingAverage = 4.2,
        commentCount = 3L,
        isBookmarked = isBookmarked
    )

    private fun getString(resId: Int): String =
        androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
            .getString(resId)
}
