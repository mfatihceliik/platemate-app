package com.mefy.platemate.presentation.features.main.discover.cityplates

sealed interface CityPlatesUiAction {
    data class PlateClicked(val plateId: String) : CityPlatesUiAction
    data object LoadMoreRequested : CityPlatesUiAction
    data object RetryClicked : CityPlatesUiAction
    data object BackClicked : CityPlatesUiAction
}
