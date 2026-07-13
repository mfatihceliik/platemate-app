package com.mefy.platemate.presentation.features.main.discover

sealed interface DiscoverUiEffect {
    data class NavigateToTrendDetail(val trendId: String) : DiscoverUiEffect
    data class NavigateToCityPlates(val cityId: Int, val cityName: String) : DiscoverUiEffect
}

