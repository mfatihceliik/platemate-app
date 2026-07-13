package com.mefy.platemate.presentation.features.main.discover.cityplates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun CityPlatesScreen(
    modifier: Modifier = Modifier,
    state: CityPlatesUiState,
    onAction: (CityPlatesUiAction) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onPlateClick = remember(onAction) {
        { id: String -> onAction(CityPlatesUiAction.PlateClicked(id)) }
    }

    val shouldLoadMore by remember(lazyListState) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            layoutInfo.totalItemsCount > 0 &&
                lastVisibleIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore, state.endReached, state.isLoadingMore) {
        if (shouldLoadMore && !state.endReached && !state.isLoadingMore) {
            onAction(CityPlatesUiAction.LoadMoreRequested)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.background(colors.background),
        contentPadding = PaddingValues(
            start = dims.spacing.s16,
            end = dims.spacing.s16,
            top = dims.spacing.s16,
            bottom = dims.spacing.s16 + innerPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        items(
            items = state.plates,
            key = { it.id },
            contentType = { "city_plate_card" }) { plate ->
            PMPlateCard(
                id = plate.id,
                rank = plate.rank,
                plateNumber = plate.plateCode,
                rating = plate.ratingText,
                commentCount = plate.reviewCount,
                searchCount = 0,
                onClick = onPlateClick,
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
    }
}

@Preview(name = "CityPlates Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun CityPlatesLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CityPlatesScreen(
            state = previewState(), onAction = {})
    }
}

@Preview(name = "CityPlates Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun CityPlatesDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        CityPlatesScreen(
            state = previewState(), onAction = {})
    }
}

private fun previewState(): CityPlatesUiState = CityPlatesUiState(
    isLoading = false,
    cityName = "İstanbul",
    plates = listOf(
        CityPlateUiModel(id = "34EK0682", rank = 1, plateCode = "34 EK 0682", ratingText = "4.8", reviewCount = 12, todayReviewCount = 4),
        CityPlateUiModel(id = "34ABC123", rank = 2, plateCode = "34 ABC 123", ratingText = "4.2", reviewCount = 9, todayReviewCount = 2),
        CityPlateUiModel(id = "34T4421", rank = 3, plateCode = "34 T 4421", ratingText = "3.9", reviewCount = 5, todayReviewCount = 1)
    )
)
