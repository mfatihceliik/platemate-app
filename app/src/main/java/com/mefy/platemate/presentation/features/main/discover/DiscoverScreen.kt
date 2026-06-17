package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMCategoryCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTrendCard
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction.FilterSelected
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    PMBaseScreen(modifier = modifier) { innerPadding ->
        Crossfade(
            targetState = state.isInitialLoading,
            label = "discover_loading_crossfade",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { isInitialLoading ->
            if (isInitialLoading) {
                DiscoverShimmerContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(DISCOVER_SHIMMER_ROOT_TAG)
                )
            } else {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onAction(DiscoverUiAction.RefreshRequested) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    DiscoverContent(
                        state = state,
                        onAction = onAction,
                        lazyListState = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoverContent(
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .testTag(DISCOVER_CONTENT_ROOT_TAG),
        contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
                Text(
                    text = stringResource(R.string.discover_header_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = colors.textPrimary
                )
                Text(
                    text = stringResource(R.string.discover_header_subtitle),
                    fontSize = 14.sp,
                    color = colors.textTertiary
                )
            }
        }

        item {
            DiscoverFilterChips(
                selectedFilter = state.selectedFilter,
                onSelected = { onAction(FilterSelected(it)) }
            )
        }

        item {
            PMText(
                text = stringResource(R.string.discover_section_trending),
                style = PMTextStyle.SectionLabel
            )
        }

        items(
            items = state.plateDetail,
            key = { it.id },
            contentType = { "trend_card" }
        ) { detail ->
            PMTrendCard(
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
            PMText(
                text = stringResource(R.string.discover_section_categories),
                style = PMTextStyle.SectionLabel
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

@Composable
private fun DiscoverFilterChips(
    selectedFilter: DiscoverFilterUi,
    onSelected: (DiscoverFilterUi) -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        items(DiscoverFilterUi.entries, key = { it.name }) { filter ->
            val isSelected = selectedFilter == filter
            val bg = if (isSelected) MaterialTheme.colorScheme.primary else colors.chipBg
            val fg = if (isSelected) MaterialTheme.colorScheme.onPrimary else colors.textSecondary

            Box(
                modifier = Modifier
                    .height(dims.sizing.chipHeight)
                    .clip(RoundedCornerShape(dims.radius.rFull))
                    .background(bg)
                    .debouncedClickable { onSelected(filter) }
                    .padding(horizontal = dims.spacing.s16),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(filter.toLabelResId()),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fg
                )
            }
        }
    }
}

@Composable
private fun DiscoverShimmerContent(
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colorScheme.surface.copy(alpha = 0.95f),
                colors.skeletonSecondary.copy(alpha = 0.45f)
            ),
            shaderColorStops = listOf(0f, 0.5f, 1f)
        )
    }
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = shimmerTheme
    )

    LazyColumn(
        modifier = modifier.background(colorScheme.background),
        contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(dims.spacing.s32),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(dims.spacing.s16),
                    shape = RoundedCornerShape(dims.radius.r8)
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
                repeat(4) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .height(dims.sizing.chipHeight)
                            .weight(1f),
                        shape = RoundedCornerShape(dims.radius.rFull)
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8)
            )
        }

        items(3) {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(dims.spacing.s12)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(dims.sizing.rankBadgeSize),
                        shape = RoundedCornerShape(dims.radius.r8)
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .size(width = 42.dp, height = 28.dp),
                        shape = RoundedCornerShape(dims.radius.r8)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(dims.spacing.s16),
                            shape = RoundedCornerShape(dims.radius.r8)
                        )
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(dims.spacing.s12),
                            shape = RoundedCornerShape(dims.radius.r8)
                        )
                    }
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r8)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                repeat(2) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .weight(1f)
                            .height(104.dp),
                        shape = RoundedCornerShape(dims.radius.r16)
                    )
                }
            }
        }
    }
}

@Composable
private fun ShimmerBlock(
    shimmer: com.valentinilk.shimmer.Shimmer,
    modifier: Modifier,
    shape: RoundedCornerShape
) {
    val colors = MaterialTheme.pmColors

    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(
                color = colors.skeleton.copy(alpha = 0.75f),
                shape = shape
            )
    )
}

private const val DISCOVER_SHIMMER_ROOT_TAG = "discover_shimmer_root"
private const val DISCOVER_CONTENT_ROOT_TAG = "discover_content_root"

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
