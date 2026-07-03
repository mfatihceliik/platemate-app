package com.mefy.platemate.presentation.features.main.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.features.main.discover.DiscoverUiAction.FilterSelected
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverCategoryGrid
import com.mefy.platemate.presentation.features.main.discover.components.DiscoverFilterChips
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun DiscoverScreen(
    modifier: Modifier = Modifier,
    state: DiscoverUiState,
    onAction: (DiscoverUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Stable, shared callbacks: cards/chips skip recomposition while data is unchanged.
    val onTrendClick = remember(onAction) {
        { id: String -> onAction(DiscoverUiAction.TrendPlateClicked(id)) }
    }
    val onFilterSelected = remember(onAction) {
        { filter: DiscoverFilterUi -> onAction(FilterSelected(filter)) }
    }
    val onCategoryClick = remember { {} }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(
            start = dims.spacing.s16,
            end = dims.spacing.s16,
            top = dims.spacing.s16,
            bottom = dims.spacing.s16 + innerPadding.calculateBottomPadding()
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
                selectedFilter = state.selectedFilter, onSelected = onFilterSelected
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
            contentType = { "trend_card" }) { detail ->
            PMPlateCard(
                id = detail.id,
                rank = detail.rank ?: 0,
                cityCode = detail.cityCode,
                plateNumber = detail.plateCode,
                rating = detail.ratingText,
                commentCount = detail.commentCount,
                searchCount = detail.searchCount,
                onClick = onTrendClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SectionLabel(
                text = stringResource(R.string.discover_section_categories),
            )
        }

        item {
            DiscoverCategoryGrid(onCategoryClick = onCategoryClick)
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
        metrics = emptyList(),
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
        cityStats = emptyList(),
        recentActivities = emptyList()
    )
}

private fun previewReportTags(): List<PlateReportTagUiModel> = listOf(
    PlateReportTagUiModel(
        code = "CUTS", label = "Cuts lanes", severity = "HIGH", colorHex = "#FF6A3D"
    )
)
