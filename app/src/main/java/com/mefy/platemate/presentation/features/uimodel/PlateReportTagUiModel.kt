package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class PlateReportTagUiModel(
    val code: String,
    val label: String,
    val severity: String,
    val colorHex: String
)

