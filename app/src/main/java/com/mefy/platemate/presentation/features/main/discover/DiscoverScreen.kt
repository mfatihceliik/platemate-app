package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction.FilterSelected
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverCityStatRow
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverForYouSection
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverHeroStats
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverRecentActivityRow
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverTopReportTypes
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverFilterButton
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeCountUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    onOpenFilter: () -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Stable, shared callbacks: cards/chips skip recomposition while data is unchanged.
    val onTrendClick = remember(onAction) {
        { id: String -> onAction(DiscoverUiAction.TrendPlateClicked(id)) }
    }
    val onBookmarkClick = remember(onAction) {
        { id: String -> onAction(DiscoverUiAction.TrendPlateBookmarkClicked(id)) }
    }
    val onFilterSelected = remember(onAction) {
        { filter: DiscoverFilterUi -> onAction(FilterSelected(filter)) }
    }
    val onCityStatClick = remember(onAction) {
        { cityId: Int, cityName: String -> onAction(DiscoverUiAction.CityStatClicked(cityId, cityName)) }
    }
    val onActivityPlateClick = remember(onAction) {
        { plateCode: String -> onAction(DiscoverUiAction.RecentActivityPlateClicked(plateCode)) }
    }

    // Sayfalama yalnizca aktif filtre varken; filtresiz ana ekran max 8 ile sinirlidir.
    val paginationEnabled = state.activeFilters.hasActiveFilters
    val shouldLoadMore by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            layoutInfo.totalItemsCount > 0 &&
                lastVisibleIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(paginationEnabled, shouldLoadMore, state.endReached, state.isLoadingMore) {
        if (paginationEnabled && shouldLoadMore && !state.endReached && !state.isLoadingMore) {
            onAction(DiscoverUiAction.LoadMoreRequested)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {

            item {
                PMSectionLabel(
                    text = stringResource(R.string.discover_header_subtitle),
                )
            }

            if (state.metrics.isNotEmpty()) {
                item(contentType = "hero_stats") {
                    DiscoverHeroStats(metrics = state.metrics)
                }
            }

            if (state.forYou != null) {
                item(contentType = "for_you_section") {
                    DiscoverForYouSection(
                        forYou = state.forYou,
                        cardStyle = state.cardStyle,
                        onPlateClick = onTrendClick,
                        onBookmarkClick = onBookmarkClick,
                        onActivityPlateClick = onActivityPlateClick
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    items(
                        DiscoverFilterUi.entries,
                        key = { it.name },
                        contentType = { "filter_chip" }
                    ) { filter ->
                        val isSelected = state.selectedFilter == filter
                        PMChip(
                            label = filter.name,
                            selected = isSelected,
                            onClick = { onFilterSelected(filter) }
                        )
                    }
                    item {

                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMSectionLabel(
                        text = stringResource(R.string.discover_section_trending),
                    )
                    DiscoverFilterButton(
                        activeCount = state.activeFilters.activeFilterCount,
                        onClick = {
                            // Taslak, filtre ekranina gitmeden once bir kez tohumlanir; alt picker'lardan
                            // donuste ekran yeniden compose olsa da secim korunur.
                            onAction(DiscoverUiAction.FilterScreenOpened)
                            onOpenFilter()
                        }
                    )
                }

            }

            itemsIndexed(
                items = state.plateDetail,
                key = { index, it -> "${it.id}_$index" },
                contentType = { _, _ -> "trend_card" }) { _, detail ->
                PMPlateCard(
                    id = detail.id,
                    rank = detail.rank ?: 0,
                    plateNumber = detail.plateCode,
                    rating = detail.ratingText,
                    commentCount = detail.commentCount,
                    searchCount = detail.searchCount,
                    onClick = onTrendClick,
                    style = state.cardStyle,
                    isBookmarked = detail.isBookmarked,
                    onBookmarkClick = onBookmarkClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.isLoadingMore) {
                item(contentType = "loading_more") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dims.spacing.s8),
                        contentAlignment = Alignment.Center
                    ) {
                        PMCircularProgressIndicator()
                    }
                }
            }

            if (state.topReportTypes.isNotEmpty()) {
                item {
                    PMSectionLabel(
                        text = stringResource(R.string.discover_section_top_reports),
                    )
                }

                item(contentType = "top_report_types") {
                    DiscoverTopReportTypes(reportTypes = state.topReportTypes)
                }
            }

            if (state.cityStats.isNotEmpty()) {
                item {
                    PMSectionLabel(
                        text = stringResource(R.string.discover_section_by_city),
                    )
                }

                itemsIndexed(
                    items = state.cityStats,
                    key = { index, it -> "city_${it.cityId}_$index" },
                    contentType = { _, _ -> "city_stat_row" }) { _, cityStat ->
                    DiscoverCityStatRow(
                        cityStat = cityStat,
                        onClick = onCityStatClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (state.recentActivities.isNotEmpty()) {
                item {
                    PMSectionLabel(
                        text = stringResource(R.string.discover_section_recent_activity),
                    )
                }

                itemsIndexed(
                    items = state.recentActivities,
                    key = { index, it -> "activity_${it.id}_$index" },
                    contentType = { _, _ -> "recent_activity_row" }) { _, activity ->
                    DiscoverRecentActivityRow(
                        activity = activity,
                        onPlateClick = onActivityPlateClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(name = "Discover Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false), onAction = {})
    }
}

@Preview(name = "Discover Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun DiscoverDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false), onAction = {})
    }
}

@Preview(name = "Discover Shimmer", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverShimmerPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = true), onAction = {})
    }
}

private fun previewState(isInitialLoading: Boolean): DiscoverUiState {
    return DiscoverUiState(
        isInitialLoading = isInitialLoading,
        selectedFilter = DiscoverFilterUi.Trend,
        metrics = listOf(
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Search,
                valueText = "128",
                labelResId = R.string.discover_metric_search_label,
                periodResId = R.string.discover_metric_today_period,
                deltaText = "+24%",
                deltaPositive = true
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Comment,
                valueText = "46",
                labelResId = R.string.discover_metric_comment_label,
                periodResId = R.string.discover_metric_today_period,
                deltaText = "-8%",
                deltaPositive = false
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Alert,
                valueText = "12",
                labelResId = R.string.discover_metric_active_alert_period,
                periodResId = R.string.discover_metric_today_period,
                deltaText = "+5%",
                deltaPositive = false
            )
        ),
        plateDetail = listOf(
            PlateDetailUiModel(
                id = "preview_1",
                rank = 1,
                plateCode = "34 EK 0682",
                cityName = "İstanbul",
                reportTags = previewReportTags(),
                ratingAverage = 4.8,
                commentCount = 12,
                cityCode = "34",
                ratingText = "4.8"
            ), PlateDetailUiModel(
                id = "preview_2",
                rank = 2,
                plateCode = "06 ABC 123",
                cityName = "Ankara",
                reportTags = previewReportTags(),
                ratingAverage = 4.6,
                commentCount = 9,
                cityCode = "06",
                ratingText = "4.6"
            ), PlateDetailUiModel(
                id = "preview_3",
                rank = 3,
                plateCode = "35 T 4421",
                cityName = "İzmir",
                reportTags = emptyList(),
                ratingAverage = 4.3,
                commentCount = 6,
                cityCode = "35",
                ratingText = "4.3"
            )
        ),
        cityStats = listOf(
            DiscoverCityStatUiModel(rank = 1, cityId = 34, cityName = "İstanbul", count = 42, progress = 1f),
            DiscoverCityStatUiModel(rank = 2, cityId = 6, cityName = "Ankara", count = 27, progress = 0.64f)
        ),
        recentActivities = listOf(
            DiscoverRecentActivityUiModel(
                id = "activity_1",
                type = RecentActivityActionType.REVIEW_ADDED,
                actorName = "fatih",
                actionText = UiText.Resource(R.string.discover_activity_review_added),
                plateCode = "34 EK 0682",
                timeAgoText = UiText.Resource(R.string.time_ago_minutes, listOf(12L))
            )
        ),
        topReportTypes = listOf(
            DiscoverReportTypeCountUiModel(code = "DANGEROUS_DRIVING", label = "Tehlikeli Sürüş", colorHex = "#E53935", count = 14),
            DiscoverReportTypeCountUiModel(code = "PARKING", label = "Hatalı Park", colorHex = "#FFB300", count = 9)
        )
    )
}

private fun previewReportTags(): List<PlateReportTagUiModel> = listOf(
    PlateReportTagUiModel(
        code = "CUTS", label = "Cuts lanes", severity = "HIGH", colorHex = "#FF6A3D"
    )
)
