package com.mefy.platemate.presentation.features.main.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMDraggableFab
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.search.components.AlarmPlateCompactCard
import com.mefy.platemate.presentation.features.main.search.components.SavedPlateCompactCard
import com.mefy.platemate.presentation.features.uimodel.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    onAction: (SearchUiAction) -> Unit,
    onNavigateToCameraScanner: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues(),
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    // Stable, paylaşılan callback'ler: chip/kart'lar veri değişmedikçe skip etsin.
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
            start = dims.spacing.s16,
            end = dims.spacing.s16,
            top = dims.spacing.s16,
            bottom = dims.spacing.s16 + innerPadding.calculateBottomPadding()
        ),
        verticalArrangement = spacedByWithFooter(dims.spacing.s16)
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

            /*if (state.plateInput.isNotBlank() && !state.isPlateValid) {
                PMText(
                    text = stringResource(R.string.search_plate_invalid_format),
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
            }*/
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
                        modifier = Modifier.debouncedClickable(onClick = onClearRecentClicked)
                    )
                }

                Spacer(modifier = Modifier.height(dims.spacing.s8))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
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
                        key = { "saved_${it.normalizedPlateCode}" },
                        contentType = { "saved_plate" }
                    ) { item ->
                        SavedPlateCompactCard(
                            modifier = Modifier.animateItem(),
                            item = item,
                            onClick = onRecentClick,
                            onBookmarkClick = onSavedBookmark
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
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    contentPadding = PaddingValues(end = dims.spacing.s16)
                ) {
                    items(
                        items = state.alarmPlates,
                        key = { "alarm_${it.normalizedPlateCode}" },
                        contentType = { "alarm_plate" }
                    ) { item ->
                        AlarmPlateCompactCard(
                            modifier = Modifier.animateItem(),
                            item = item,
                            onClick = onRecentClick,
                            onRemoveClick = onAlarmRemove
                        )
                    }
                }
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

    PMDraggableFab(
        onClick = onNavigateToCameraScanner,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(
                end = dims.spacing.s16,
                bottom = innerPadding.calculateBottomPadding() + dims.spacing.s24
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
