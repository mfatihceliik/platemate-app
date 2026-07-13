package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverReportTypeCountUiModel(
    val code: String,
    val label: String,
    val colorHex: String,
    val count: Int
)
