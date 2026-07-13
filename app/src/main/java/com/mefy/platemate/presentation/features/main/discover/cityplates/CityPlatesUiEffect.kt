package com.mefy.platemate.presentation.features.main.discover.cityplates

sealed interface CityPlatesUiEffect {
    data object NavigateBack : CityPlatesUiEffect
    data class NavigateToPlateDetail(val plateCode: String) : CityPlatesUiEffect
}
