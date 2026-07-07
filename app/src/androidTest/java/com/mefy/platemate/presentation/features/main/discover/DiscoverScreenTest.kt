package com.mefy.platemate.presentation.features.main.discover

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
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
        val state = sampleState(isInitialLoading = true)

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
        val state = sampleState(isInitialLoading = false)

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
        val state = sampleState(isInitialLoading = false)

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                DiscoverScreen(
                    state = state,
                    onAction = { emittedActions.add(it) }
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.discover_filter_careless)).performClick()

        composeRule.runOnIdle {
            assertEquals(
                DiscoverUiAction.FilterSelected(DiscoverFilterUi.Careless),
                emittedActions.lastOrNull()
            )
        }
    }

    @Test
    fun trendPlateClick_triggersAction_whenLoaded() {
        val state = sampleState(isInitialLoading = false)
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
        val state = sampleState(isInitialLoading = true)
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
        ApplicationProvider.getApplicationContext<Context>().getString(resId)

    private fun sampleState(isInitialLoading: Boolean): DiscoverUiState {
        return DiscoverUiState(
            isInitialLoading = isInitialLoading,
            selectedFilter = DiscoverFilterUi.Trend,
            metrics = listOf(
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Search,
                    valueText = "142",
                    labelResId = R.string.discover_metric_search_label,
                    periodResId = R.string.discover_metric_today_period
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Comment,
                    valueText = "1.2K",
                    labelResId = R.string.discover_metric_comment_label,
                    periodResId = R.string.discover_metric_week_period
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Alert,
                    valueText = "38",
                    labelResId = R.string.discover_metric_report_label,
                    periodResId = R.string.discover_metric_active_alert_period
                )
            ),
            plateDetail = listOf(
                PlateDetailUiModel(
                    id = "trend_34_abc_123",
                    rank = 1,
                    plateCode = "34 ABC 123",
                    cityName = "Istanbul",
                    reportTags = listOf(
                        PlateReportTagUiModel(
                            code = "CUTS",
                            label = "Cuts lanes",
                            severity = "HIGH",
                            colorHex = "#FF6A3D"
                        ),
                        PlateReportTagUiModel(
                            code = "SPEEDING",
                            label = "Speeding",
                            severity = "MEDIUM",
                            colorHex = "#FFB300"
                        )
                    ),
                    ratingAverage = 2.3,
                    commentCount = 3
                )
            ),
            cityStats = listOf(
                DiscoverCityStatUiModel(
                    rank = 1,
                    cityName = "Istanbul",
                    count = 312,
                    progress = 1f
                )
            ),
            recentActivities = listOf(
                DiscoverRecentActivityUiModel(
                    id = "activity_ahmet_comment",
                    type = RecentActivityActionType.REVIEW_ADDED,
                    actorName = "Ahmet",
                    actionText = "commented on",
                    plateCode = "34 ABC 123",
                    timeAgoText = "2 min ago"
                )
            )
        )
    }
}

