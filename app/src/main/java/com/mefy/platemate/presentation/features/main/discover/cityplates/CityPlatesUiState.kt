package com.mefy.platemate.presentation.features.main.discover.cityplates

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class CityPlateUiModel(
    val id: String,
    val rank: Int,
    val plateCode: String,
    val ratingText: String,
    val reviewCount: Long,
    val todayReviewCount: Long
)

@Immutable
data class CityPlatesUiState(
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val errorMessage: UiText? = null,
    val cityName: String = "",
    val plates: List<CityPlateUiModel> = emptyList(),
    val page: Int = 0,
    val endReached: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && errorMessage == null && plates.isEmpty()
}
