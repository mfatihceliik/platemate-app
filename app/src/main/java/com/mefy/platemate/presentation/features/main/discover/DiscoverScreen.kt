package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.presentation.common.state.ScreenStatus
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCategoryCard
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction.FilterSelected
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverFilterChips
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverShimmerContent
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.discover_header_title),
            alignment = PMTopBarAlignment.Start
        ),
        status = status,
        onRetry = { onAction(DiscoverUiAction.RetryClicked) },
        loading = { innerPadding ->
            DiscoverShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(DiscoverUiAction.RefreshRequested) },
            modifier = Modifier.fillMaxSize()
        ) {
            DiscoverContent(
                state = state,
                onAction = onAction,
                lazyListState = lazyListState,
                bottomInset = innerPadding.calculateBottomPadding(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun DiscoverContent(
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    lazyListState: LazyListState,
    bottomInset: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(colors.background),
        contentPadding = PaddingValues(
            start = dims.spacing.s16,
            end = dims.spacing.s16,
            top = dims.spacing.s16,
            bottom = dims.spacing.s16 + bottomInset
        ),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            SectionLabel(
                text = stringResource(R.string.discover_header_subtitle),
            )
        }

        item {
            DiscoverFilterChips(
                selectedFilter = state.selectedFilter,
                onSelected = { onAction(FilterSelected(it)) }
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.discover_section_trending),
            )
        }

        items(
            items = state.plateDetail,
            key = { it.id },
            contentType = { "trend_card" }
        ) { detail ->
            PMPlateCard(
                rank = detail.rank ?: 0,
                cityCode = detail.plateCode.take(2),
                plateNumber = detail.plateCode,
                rating = String.format("%.1f", detail.ratingAverage),
                extra = stringResource(R.string.discover_comment_count, detail.commentCount),
                onClick = { onAction(DiscoverUiAction.TrendPlateClicked(detail.id)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.discover_section_categories),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMCategoryCard(
                    title = stringResource(R.string.discover_category_kindest),
                    count = stringResource(R.string.discover_category_kindest_count),
                    backgroundColor = colors.categoryTealBg,
                    foregroundColor = colors.categoryTealFg,
                    iconColor = colors.categoryTealIcon,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                PMCategoryCard(
                    title = stringResource(R.string.discover_category_quickrespond),
                    count = stringResource(R.string.discover_category_quickrespond_count),
                    backgroundColor = colors.categoryIndigoBg,
                    foregroundColor = colors.categoryIndigoFg,
                    iconColor = colors.categoryIndigoIcon,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMCategoryCard(
                    title = stringResource(R.string.discover_category_careful),
                    count = stringResource(R.string.discover_category_careful_count),
                    backgroundColor = colors.categoryOrangeBg,
                    foregroundColor = colors.categoryOrangeFg,
                    iconColor = colors.categoryOrangeIcon,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                PMCategoryCard(
                    title = stringResource(R.string.discover_category_helpful),
                    count = stringResource(R.string.discover_category_helpful_count),
                    backgroundColor = colors.categoryGreenBg,
                    foregroundColor = colors.categoryGreenFg,
                    iconColor = colors.categoryGreenIcon,
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Preview(name = "Discover Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Discover Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun DiscoverDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Discover Shimmer", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun DiscoverShimmerPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = true),
            onAction = {}
        )
    }
}

private fun previewState(isInitialLoading: Boolean): DiscoverUiState {
    return DiscoverUiState(
        isInitialLoading = isInitialLoading,
        selectedFilter = DiscoverFilterUi.Trend,
        metrics = emptyList(),
        plateDetail = listOf(
            PlateDetailUiModel(
                id = "preview_1",
                rank = 1,
                plateCode = "34 EK 0682",
                cityName = "İstanbul",
                reportTags = previewReportTags(),
                ratingAverage = 4.8,
                commentCount = 12
            ),
            PlateDetailUiModel(
                id = "preview_2",
                rank = 2,
                plateCode = "06 ABC 123",
                cityName = "Ankara",
                reportTags = previewReportTags(),
                ratingAverage = 4.6,
                commentCount = 9
            ),
            PlateDetailUiModel(
                id = "preview_3",
                rank = 3,
                plateCode = "35 T 4421",
                cityName = "İzmir",
                reportTags = emptyList(),
                ratingAverage = 4.3,
                commentCount = 6
            )
        ),
        cityStats = emptyList(),
        recentActivities = emptyList()
    )
}

private fun previewReportTags(): List<PlateReportTagUiModel> = listOf(
    PlateReportTagUiModel(
        code = "CUTS",
        label = "Cuts lanes",
        severity = "HIGH",
        colorHex = "#FF6A3D"
    )
)
