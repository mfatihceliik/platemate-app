package com.mefy.platemate.presentation.features.main.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.search.components.SearchEmptyState
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMDraggableFab
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateCompactCard
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    onAction: (SearchUiAction) -> Unit,
    onNavigateToCameraScanner: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues(),
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    val sizing = PMTheme.sizing

    val onRecentClick = remember(onAction) { { code: String -> onAction(SearchUiAction.RecentItemClicked(code)) } }
    val onSavedBookmark = remember(onAction) { { norm: String -> onAction(SearchUiAction.SavedPlateBookmarkClicked(norm)) } }
    val onAlarmRemove = remember(onAction) { { norm: String -> onAction(SearchUiAction.AlarmPlateRemoveClicked(norm)) } }
    val onPlateInputChanged = remember(onAction) { { value: String -> onAction(SearchUiAction.PlateInputChanged(value)) } }
    val onSearchClicked = remember(onAction) { { onAction(SearchUiAction.SearchClicked) } }
    val onClearRecentClicked = remember(onAction) { { onAction(SearchUiAction.ClearRecentClicked) } }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding,
        verticalArrangement = spacedByWithFooter(spacing.s16)
    ) {
        item {

            val errorText = state.plateInput.isNotBlank() && !state.isPlateValid
            val isSuccess = state.detectedCityName != null

            PMSearchBar(
                query = state.plateInput,
                onQueryChange = onPlateInputChanged,
                onSearch = onSearchClicked,
                isSearchActionEnabled = state.isPlateValid,
                isLoading = state.isSearching,
                enabled = !state.isSearching,
                modifier = Modifier.fillMaxWidth(),
                supportingText = if(isSuccess) stringResource(R.string.search_detected_city_plate, state.detectedCityName) else null,
                isSuccess = isSuccess,
                errorText = if(errorText) stringResource(R.string.search_plate_invalid_format) else null,
                isError = errorText,
            )
        }

        if (state.recentSearches.isEmpty() && state.bookmarkedPlates.isEmpty() && state.alarmPlates.isEmpty()) {
            item {
                SearchEmptyState()
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
                        fontSize = fontSize.md,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary,
                        modifier = Modifier.debouncedClickable(onClick = onClearRecentClicked)
                    )
                }

                Spacer(modifier = Modifier.height(spacing.s8))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    state.recentSearches.forEach { item ->
                        key(item.normalizedPlateCode) {
                            PMChip(
                                label = item.plateCode,
                                onClick = { onRecentClick(item.plateCode) }
                            )
                        }
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
                        fontSize = fontSize.md,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary,
                        modifier = Modifier.debouncedClickable { }
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    contentPadding = PaddingValues(end = spacing.s16)
                ) {
                    items(
                        items = state.bookmarkedPlates,
                        key = { "saved_${it.normalizedPlateCode}" },
                        contentType = { "saved_plate" }
                    ) { item ->
                        PMPlateCompactCard(
                            modifier = Modifier.animateItem(),
                            item = item,
                            trailingIcon = if (item.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            trailingIconTint = if (item.isBookmarked) colors.primary else colors.iconDefault,
                            onClick = onRecentClick,
                            onTrailingIconClick = onSavedBookmark
                        )
                    }
                }
            }
        }

        if (state.alarmPlates.isNotEmpty()) {
            item {
                PMText(
                    text = stringResource(R.string.search_section_alarms),
                    style = PMTextStyle.SectionLabel
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                    contentPadding = PaddingValues(end = spacing.s16)
                ) {
                    items(
                        items = state.alarmPlates,
                        key = { "alarm_${it.normalizedPlateCode}" },
                        contentType = { "alarm_plate" }
                    ) { item ->
                        PMPlateCompactCard(
                            modifier = Modifier.animateItem(),
                            item = item,
                            trailingIcon = Icons.Filled.NotificationsActive,
                            trailingIconTint = colors.primary,
                            onClick = onRecentClick,
                            onTrailingIconClick = onAlarmRemove
                        )
                    }
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
                    PMIcon(
                        imageVector = Icons.Outlined.Info,
                    )
                    PMText(
                        text = stringResource(R.string.search_safety_banner),
                        fontSize = fontSize.sm,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }

    val navBottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val fabBottomPadding = sizing.bottomBarHeight + navBottomInset
    PMDraggableFab(
        onClick = onNavigateToCameraScanner,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = spacing.s16,
                bottom = fabBottomPadding + spacing.s24
            )
    )
}
}

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
            onAction = {},
            onNavigateToCameraScanner = {}
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
            onAction = {},
            onNavigateToCameraScanner = {}
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
