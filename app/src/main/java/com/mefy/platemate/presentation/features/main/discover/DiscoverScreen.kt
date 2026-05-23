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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.components.PlateCard
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction.FilterSelected
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
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
    val colorScheme = MaterialTheme.colorScheme

    Crossfade(
        targetState = state.isInitialLoading,
        label = "discover_loading_crossfade",
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) { isInitialLoading ->
        if (isInitialLoading) {
            DiscoverShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(DiscoverUiAction.RefreshRequested) },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                DiscoverContent(
                    state = state,
                    onAction = onAction,
                    lazyListState = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                )
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
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    LazyColumn(
        state = lazyListState,
        modifier = modifier.background(colorScheme.background),
        contentPadding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
                PMText(
                    text = stringResource(R.string.discover_header_title),
                    style = PMTextStyle.Headline,
                    color = colorScheme.onBackground
                )
                PMText(
                    text = stringResource(R.string.discover_header_subtitle),
                    style = PMTextStyle.Body,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            MetricsSection(metrics = state.metrics)
        }

        item {
            DiscoverFilterChips(
                selectedFilter = state.selectedFilter,
                onSelected = { onAction(FilterSelected(it)) }
            )
        }

        item {
            SectionHeader(
                icon = state.selectedFilter.toSectionIcon(),
                title = stringResource(state.selectedFilter.toSectionTitleResId())
            )
        }

        items(
            items = state.plateDetail,
            key = { it.id },
            contentType = { "plate_detail" }
        ) { detail ->
            PlateCard(
                plateCode = detail.plateCode,
                onClick = { onAction(DiscoverUiAction.TrendPlateClicked(detail.id)) },
                rank = detail.rank,
                cityName = detail.cityName,
                reportTags = detail.reportTags,
                ratingAverage = detail.ratingAverage,
                commentCount = detail.commentCount,
            )
        }

        item {
            HorizontalDivider(color = colorScheme.outlineVariant)
        }

        item {
            SectionHeader(
                icon = Icons.Filled.Search,
                title = stringResource(R.string.discover_section_by_city)
            )
        }

        items(
            items = state.cityStats,
            key = { "${it.rank}_${it.cityName}" },
            contentType = { "city_stat" }
        ) { cityStat ->
            CityStatCard(cityStat = cityStat)
        }

        item {
            SectionHeader(
                icon = Icons.Filled.AccessTime,
                title = stringResource(R.string.discover_section_recent_activity)
            )
        }

        items(
            items = state.recentActivities,
            key = { it.id },
            contentType = { "recent_activity" }
        ) { activity ->
            RecentActivityRow(activity = activity)
        }
    }
}

@Composable
private fun DiscoverShimmerContent(
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colorScheme.surfaceVariant.copy(alpha = 0.55f),
                colorScheme.surface.copy(alpha = 0.95f),
                colorScheme.outlineVariant.copy(alpha = 0.45f)
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
        contentPadding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .height(spacing.s32),
                        shape = RoundedCornerShape(radius.r8)
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.62f)
                            .height(spacing.s16),
                        shape = RoundedCornerShape(radius.r8)
                    )
                }

                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.16f)
                        .height(spacing.s24),
                    shape = RoundedCornerShape(radius.r12)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s10)
            ) {
                repeat(3) {
                    PMCard(
                        modifier = Modifier.weight(1f),
                        padding = PaddingValues(horizontal = spacing.s10, vertical = spacing.s12)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier
                                    .size(spacing.s32),
                                shape = RoundedCornerShape(radius.r10)
                            )
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(spacing.s20),
                                shape = RoundedCornerShape(radius.r8)
                            )
                            ShimmerBlock(
                                shimmer = shimmer,
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .height(spacing.s12),
                                shape = RoundedCornerShape(radius.r8)
                            )
                        }
                    }
                }
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                items(4) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillParentMaxWidth(0.24f)
                            .height(spacing.s32),
                        shape = RoundedCornerShape(radius.r18)
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.38f)
                    .height(spacing.s24),
                shape = RoundedCornerShape(radius.r8)
            )
        }

        items(4) {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = spacing.s12, vertical = spacing.s10)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.18f)
                                .height(spacing.s16),
                            shape = RoundedCornerShape(radius.r8)
                        )
                    }
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.16f)
                            .height(spacing.s20),
                        shape = RoundedCornerShape(radius.r8)
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.32f)
                    .height(spacing.s22),
                shape = RoundedCornerShape(radius.r8)
            )
        }

        items(3) {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = spacing.s12, vertical = spacing.s10)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.42f)
                                .height(spacing.s18),
                            shape = RoundedCornerShape(radius.r8)
                        )
                        ShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.12f)
                                .height(spacing.s18),
                            shape = RoundedCornerShape(radius.r8)
                        )
                    }
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(spacing.s8),
                        shape = RoundedCornerShape(radius.r8)
                    )
                }
            }
        }

        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.34f)
                    .height(spacing.s22),
                shape = RoundedCornerShape(radius.r8)
            )
        }

        items(4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s10),
                verticalAlignment = Alignment.Top
            ) {
                ShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier.size(spacing.s32),
                    shape = RoundedCornerShape(radius.r10)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(spacing.s16),
                        shape = RoundedCornerShape(radius.r8)
                    )
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(0.28f)
                            .height(spacing.s12),
                        shape = RoundedCornerShape(radius.r8)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = spacing.s8),
                        color = colorScheme.outlineVariant
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
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(
                color = colorScheme.surfaceVariant.copy(alpha = 0.75f),
                shape = shape
            )
    )
}

@Composable
private fun MetricsSection(
    metrics: List<DiscoverMetricUiModel>
) {
    val spacing = MaterialTheme.pmDimensions.spacing

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.s10)
    ) {
        metrics.forEach { metric ->
            PMCard(
                modifier = Modifier.weight(1f),
                padding = PaddingValues(horizontal = spacing.s10, vertical = spacing.s12)
            ) {
                MetricCardContent(metric = metric)
            }
        }
    }
}

@Composable
private fun MetricCardContent(
    metric: DiscoverMetricUiModel
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius

    Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
        Box(
            modifier = Modifier
                .size(spacing.s32)
                .background(
                    color = colorScheme.primaryContainer,
                    shape = RoundedCornerShape(radius.r10)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = metric.type.toMetricIcon(),
                contentDescription = null,
                tint = colorScheme.primary
            )
        }

        PMText(
            text = metric.valueText,
            style = PMTextStyle.Headline,
            color = colorScheme.onSurface
        )
        PMText(
            text = stringResource(metric.labelResId),
            style = PMTextStyle.Caption,
            color = colorScheme.onSurfaceVariant
        )
        PMText(
            text = stringResource(metric.periodResId),
            style = PMTextStyle.Caption,
            color = colorScheme.primary
        )
    }
}

@Composable
private fun DiscoverFilterChips(
    selectedFilter: DiscoverFilterUi,
    onSelected: (DiscoverFilterUi) -> Unit
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        items(DiscoverFilterUi.entries, key = { it.name }) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onSelected(filter) },
                label = {
                    PMText(
                        text = stringResource(filter.toLabelResId()),
                        style = PMTextStyle.Label
                    )
                },
                leadingIcon = if (selectedFilter == filter) {
                    { Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null) }
                } else {
                    null
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorScheme.primaryContainer,
                    selectedLabelColor = colorScheme.primary,
                    selectedLeadingIconColor = colorScheme.primary,
                    containerColor = colorScheme.surface,
                    labelColor = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}



@Composable
private fun CityStatCard(
    cityStat: DiscoverCityStatUiModel
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    PMCard(
        modifier = Modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = spacing.s12, vertical = spacing.s10)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = stringResource(R.string.discover_rank_format, cityStat.rank),
                        style = PMTextStyle.Label,
                        color = colorScheme.onSurfaceVariant
                    )
                    PMText(
                        text = cityStat.cityName,
                        style = PMTextStyle.Title,
                        color = colorScheme.onSurface
                    )
                }
                PMText(
                    text = cityStat.count.toString(),
                    style = PMTextStyle.Label,
                    color = colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = { cityStat.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(spacing.s8),
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun RecentActivityRow(
    activity: DiscoverRecentActivityUiModel
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.s4),
        horizontalArrangement = Arrangement.spacedBy(spacing.s10),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(spacing.s32)
                .background(
                    color = colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(radius.r10)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = activity.type.toActivityIcon(),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(spacing.s16)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s2)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(activity.actorName)
                    append(" ")
                    append(activity.actionText)
                    append(" ")
                    withStyle(style = SpanStyle(color = colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                        append(activity.plateCode)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onSurface
            )
            PMText(
                text = activity.timeAgoText,
                style = PMTextStyle.Caption,
                color = colorScheme.onSurfaceVariant
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = spacing.s8),
                color = colorScheme.outlineVariant
            )
        }
    }
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val colorScheme = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.s6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(spacing.s18)
        )
        PMText(
            text = title,
            style = PMTextStyle.Title,
            color = colorScheme.onBackground
        )
    }
}



@Preview(name = "Discover Light", showBackground = true, backgroundColor = 0xFFF3F6FF)
@Composable
private fun DiscoverLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Discover Dark", showBackground = true, backgroundColor = 0xFF07153A)
@Composable
private fun DiscoverDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DiscoverScreen(
            state = previewState(isInitialLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Discover Shimmer Dark", showBackground = true, backgroundColor = 0xFF07153A)
@Composable
private fun DiscoverShimmerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
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
                id = "preview_1",
                rank = 1,
                plateCode = "34 ABC 123",
                cityName = "Ä°stanbul",
                reportTags = previewReportTags(),
                ratingAverage = 2.3,
                commentCount = 3
            )
        ),
        cityStats = listOf(
            DiscoverCityStatUiModel(
                rank = 1,
                cityName = "Ä°stanbul",
                count = 312,
                progress = 1f
            )
        ),
        recentActivities = listOf(
            DiscoverRecentActivityUiModel(
                id = "preview_activity",
                type = RecentActivityActionType.REVIEW_ADDED,
                actorName = "Fatih Ã‡.",
                actionText = "commented on",
                plateCode = "34 ABC 123",
                timeAgoText = "2 min ago"
            )
        )
    )
}

private fun previewReportTags(): List<PlateReportTagUiModel> = listOf(
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
)

