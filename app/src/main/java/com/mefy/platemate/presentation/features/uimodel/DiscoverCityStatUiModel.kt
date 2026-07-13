package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverCityStatUiModel(
    val rank: Int,
    val cityId: Int,
    val cityName: String,
    val count: Int,
    val progress: Float
)
