package com.mefy.platemate.presentation.features.main.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateBadge
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    onAction: (SearchUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.search_header_title)
        )
    ) { innerPadding ->
        Crossfade(
            targetState = state.isInitialLoading,
            label = "search_loading_crossfade",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { isInitialLoading ->
            if (isInitialLoading) {
                SearchShimmerContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(SEARCH_SHIMMER_ROOT_TAG)
                )
            } else {
                SearchContent(
                    state = state,
                    onAction = onAction,
                    lazyListState = lazyListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchContent(
    state: SearchUiState,
    onAction: (SearchUiAction) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    LazyColumn(
        state = lazyListState,
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            PMSearchBar(
                query = state.plateInput,
                onQueryChange = { onAction(SearchUiAction.PlateInputChanged(it)) },
                onSearch = { onAction(SearchUiAction.SearchClicked) },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.plateInput.isNotBlank() && !state.isPlateValid) {
                PMText(
                    text = stringResource(R.string.search_plate_invalid_format),
                    fontSize = dims.fontSize.sm,
                    color = colors.error,
                    modifier = Modifier.padding(top = dims.spacing.s8, start = dims.spacing.s4)
                )
            } else if (state.formMessage != null) {
                PMText(
                    text = state.formMessage.resolve(),
                    fontSize = dims.fontSize.sm,
                    color = colors.error,
                    modifier = Modifier.padding(top = dims.spacing.s8, start = dims.spacing.s4)
                )
            }

            if (state.detectedCityName != null) {
                PMText(
                    text = stringResource(R.string.search_detected_city_plate, state.detectedCityName),
                    fontSize = dims.fontSize.sm,
                    color = colors.textTertiary,
                    modifier = Modifier.padding(top = dims.spacing.s8, start = dims.spacing.s4)
                )
            }
        }

        if (state.recentSearches.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = stringResource(R.string.search_section_recent),
                        style = PMTextStyle.SectionLabel
                    )
                    PMText(
                        text = stringResource(R.string.search_recent_clear),
                        fontSize = dims.fontSize.md,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary,
                        modifier = Modifier.debouncedClickable {
                            onAction(SearchUiAction.ClearRecentClicked)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(dims.spacing.s8))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    state.recentSearches.forEach { item ->
                        RecentChip(
                            plateCode = item.plateCode,
                            onClick = { onAction(SearchUiAction.RecentItemClicked(item.plateCode)) }
                        )
                    }
                }
            }
        }

        if (state.bookmarkedPlates.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = stringResource(R.string.search_section_saved),
                        style = PMTextStyle.SectionLabel
                    )
                    PMText(
                        text = stringResource(R.string.search_saved_see_all),
                        fontSize = dims.fontSize.md,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary,
                        modifier = Modifier.debouncedClickable { }
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    contentPadding = PaddingValues(end = dims.spacing.s16)
                ) {
                    items(
                        items = state.bookmarkedPlates,
                        key = { "saved_${it.normalizedPlateCode}" }
                    ) { item ->
                        SavedPlateCompactCard(
                            item = item,
                            onClick = { onAction(SearchUiAction.RecentItemClicked(item.plateCode)) },
                            onBookmarkClick = {
                                onAction(SearchUiAction.SavedPlateBookmarkClicked(item.normalizedPlateCode))
                            }
                        )
                    }
                }
            }
        }

        if (state.recentSearches.isEmpty() && state.bookmarkedPlates.isEmpty()) {
            item {
                SearchEmptyState()
            }
        }

        item {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMIcon(
                        imageVector = Icons.Outlined.Info,
                    )
                    PMText(
                        text = stringResource(R.string.search_safety_banner),
                        fontSize = dims.fontSize.sm,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentChip(
    plateCode: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Box(
        modifier = Modifier
            .height(dims.sizing.chipHeight)
            .clip(RoundedCornerShape(dims.radius.rFull))
            .background(colors.surfaceVariant)
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = dims.spacing.s16),
        contentAlignment = Alignment.Center
    ) {
        PMText(
            text = plateCode,
            fontSize = dims.fontSize.md,
            fontWeight = FontWeight.SemiBold,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun SavedPlateCompactCard(
    item: SearchRecentUiModel,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    PMCard(
        modifier = Modifier.width(dims.sizing.savedPlateCardWidth),
        padding = PaddingValues(dims.spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .debouncedClickable(onClick = onClick),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(
                    cityCode = item.plateCode.take(2),
                    size = PlateBadgeSize.Small
                )
                PMIcon(
                    imageVector = if (item.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    tint = if (item.isBookmarked) colors.primary else colors.textLabel,
                    size = dims.sizing.iconXl,
                    modifier = Modifier.debouncedClickable(onClick = onBookmarkClick)
                )
            }

            PMText(
                text = item.plateCode,
                fontSize = dims.fontSize.lg,
                color = colors.textPrimary,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                if (item.ratingAverage > 0) {
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = colors.star,
                    )
                    PMText(
                        text = String.format("%.1f", item.ratingAverage),
                        fontSize = dims.fontSize.md,
                        maxLines = 1,
                        color = colors.textTertiary
                    )
                }
                if (item.cityName != null) {
                    PMText(
                        text = "· ${item.cityName}",
                        fontSize = dims.fontSize.md,
                        color = colors.textLabel,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchEmptyState() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Outlined.Search,
            )
        }

        PMText(
            text = stringResource(R.string.search_empty_title),
            fontSize = dims.fontSize.sm,
            color = colors.textPrimary
        )
        PMText(
            text = stringResource(R.string.search_empty_subtitle),
            fontSize = dims.fontSize.sm,
            color = colors.textTertiary
        )
    }
}

@Composable
private fun SearchShimmerContent(
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.pmDimensions.spacing
    val radius = MaterialTheme.pmDimensions.radius
    val sizing = MaterialTheme.pmDimensions.sizing
    val colorScheme = MaterialTheme.pmColors
    val colors = MaterialTheme.pmColors

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colors.surface.copy(alpha = 0.95f),
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
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        item {
            SearchShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(spacing.s32),
                shape = RoundedCornerShape(radius.r8)
            )
        }

        item {
            SearchShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(spacing.s64),
                shape = RoundedCornerShape(radius.r16)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .height(spacing.s16),
                    shape = RoundedCornerShape(radius.r8)
                )
                SearchShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.16f)
                        .height(spacing.s16),
                    shape = RoundedCornerShape(radius.r8)
                )
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                repeat(3) {
                    SearchShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(90.dp)
                            .height(spacing.s32),
                        shape = RoundedCornerShape(radius.rFull)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(spacing.s16),
                    shape = RoundedCornerShape(radius.r8)
                )
                SearchShimmerBlock(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth(0.12f)
                        .height(spacing.s16),
                    shape = RoundedCornerShape(radius.r8)
                )
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                repeat(3) {
                    SearchShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .width(sizing.savedPlateCardWidth)
                            .height(spacing.s48 + spacing.s64),
                        shape = RoundedCornerShape(radius.r16)
                    )
                }
            }
        }

        item {
            PMCard(
                modifier = Modifier.fillMaxWidth(),
                padding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s12)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier.size(spacing.s24),
                        shape = RoundedCornerShape(radius.r8)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(spacing.s8)
                    ) {
                        SearchShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.86f)
                                .height(spacing.s12),
                            shape = RoundedCornerShape(radius.r8)
                        )
                        SearchShimmerBlock(
                            shimmer = shimmer,
                            modifier = Modifier
                                .fillMaxWidth(0.62f)
                                .height(spacing.s12),
                            shape = RoundedCornerShape(radius.r8)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchShimmerBlock(
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

private const val SEARCH_SHIMMER_ROOT_TAG = "search_shimmer_root"

@Preview(name = "Search Screen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SearchScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(
                plateInput = "34 ABC 123",
                isPlateValid = true,
                detectedCityName = "İstanbul",
                recentSearches = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    ),
                    SearchRecentUiModel(
                        normalizedPlateCode = "34XYZ123",
                        plateCode = "34 XYZ 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 3.3,
                        commentCount = 3,
                        isBookmarked = false
                    ),
                    SearchRecentUiModel(
                        normalizedPlateCode = "06AB1234",
                        plateCode = "06 AB 1234",
                        cityName = "Ankara",
                        reportTags = emptyList(),
                        ratingAverage = 4.1,
                        commentCount = 7,
                        isBookmarked = false
                    )
                ),
                bookmarkedPlates = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    ),
                    SearchRecentUiModel(
                        normalizedPlateCode = "34XYZ123",
                        plateCode = "34 XYZ 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 3.3,
                        commentCount = 3,
                        isBookmarked = true
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Search Screen Empty", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SearchScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(),
            onAction = {}
        )
    }
}

@Preview(name = "Search Screen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SearchScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(
                recentSearches = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    )
                ),
                bookmarkedPlates = listOf(
                    SearchRecentUiModel(
                        normalizedPlateCode = "34ABC123",
                        plateCode = "34 ABC 123",
                        cityName = "İstanbul",
                        reportTags = previewReportTags(),
                        ratingAverage = 2.3,
                        commentCount = 1,
                        isBookmarked = true
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "Search Screen Shimmer", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SearchScreenShimmerPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(isInitialLoading = true),
            onAction = {}
        )
    }
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
