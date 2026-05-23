package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoverScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun discoverScreen_loadingState_showsShimmerAndHidesContent() {
        val state = sampleState(isLoading = true)

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = {}
                )
            }
        }

        composeRule.onNodeWithTag("discover_shimmer_root").assertIsDisplayed()
        composeRule.onAllNodesWithTag("discover_content_root").assertCountEquals(0)
        composeRule.onAllNodesWithText("34 ABC 123").assertCountEquals(0)
    }

    @Test
    fun discoverScreen_displaysCoreSections_whenLoaded() {
        val state = sampleState(isLoading = false)

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = {}
                )
            }
        }

        composeRule.onNodeWithTag("discover_content_root").assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.discover_header_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.discover_section_trend_plates)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.discover_section_by_city)).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.discover_section_recent_activity)).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun filterChipSelection_emitsFilterAction() {
        val emittedActions = mutableListOf<DiscoverUiAction>()
        val state = sampleState(isLoading = false)

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = { emittedActions.add(it) }
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.discover_filter_dangerous)).performClick()

        composeRule.runOnIdle {
            assertEquals(
                DiscoverUiAction.FilterSelected(DiscoverTrendFilter.Dangerous),
                emittedActions.lastOrNull()
            )
        }
    }

    @Test
    fun trendPlateClick_triggersAction_whenLoaded() {
        val state = sampleState(isLoading = false)
        var clickedAction: DiscoverUiAction? = null

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = { action ->
                        if (action is DiscoverUiAction.TrendPlateClicked) {
                            clickedAction = action
                        }
                    }
                )
            }
        }

        composeRule.onNodeWithText("34 ABC 123").performClick()

        composeRule.runOnIdle {
            assertEquals(
                DiscoverUiAction.TrendPlateClicked("trend_34_abc_123"),
                clickedAction
            )
        }
    }

    @Test
    fun loadingState_doesNotEmitTrendClickAction() {
        val state = sampleState(isLoading = true)
        var clickedAction: DiscoverUiAction? = null

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = { action ->
                        if (action is DiscoverUiAction.TrendPlateClicked) {
                            clickedAction = action
                        }
                    }
                )
            }
        }

        composeRule.onAllNodesWithText("34 ABC 123").assertCountEquals(0)
        composeRule.runOnIdle {
            assertEquals(null, clickedAction)
        }
    }

    private fun getString(resId: Int): String =
        androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
            .getString(resId)

    private fun sampleState(isLoading: Boolean): DiscoverUiState {
        return DiscoverUiState(
            isLoading = isLoading,
            selectedFilter = DiscoverTrendFilter.Trend,
            metrics = listOf(
                DiscoverMetricUiModel(
                    type = DiscoverMetricType.Search,
                    valueText = "142",
                    labelResId = R.string.discover_metric_search_label,
                    periodResId = R.string.discover_metric_today_period
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricType.Comment,
                    valueText = "1.2K",
                    labelResId = R.string.discover_metric_comment_label,
                    periodResId = R.string.discover_metric_week_period
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricType.Alert,
                    valueText = "38",
                    labelResId = R.string.discover_metric_alert_label,
                    periodResId = R.string.discover_metric_active_alert_period
                )
            ),
            trendPlates = listOf(
                DiscoverTrendPlateUiModel(
                    id = "trend_34_abc_123",
                    rank = 1,
                    plateCode = "34 ABC 123",
                    cityResId = R.string.discover_city_istanbul,
                    primaryTagResId = R.string.discover_tag_scissor_driver,
                    secondaryTagResId = R.string.discover_tag_speeding,
                    scoreText = "2.3",
                    commentCount = 3,
                    category = DiscoverTrendCategory.Dangerous
                )
            ),
            cityStats = listOf(
                DiscoverCityStatUiModel(
                    rank = 1,
                    cityResId = R.string.discover_city_istanbul,
                    count = 312,
                    progress = 1f
                )
            ),
            recentActivities = listOf(
                DiscoverRecentActivityUiModel(
                    id = "activity_ahmet_comment",
                    type = DiscoverActivityType.Comment,
                    actorResId = R.string.discover_activity_actor_ahmet,
                    actionResId = R.string.discover_activity_action_commented,
                    plateCode = "34 ABC 123",
                    timeAgoResId = R.string.discover_time_2_min_ago
                )
            )
        )
    }
}
