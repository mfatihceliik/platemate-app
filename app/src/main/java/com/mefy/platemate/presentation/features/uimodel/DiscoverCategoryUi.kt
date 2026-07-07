package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class DiscoverCategoryUi(
    val titleResId: Int,
    val countResId: Int,
    val backgroundColor: Color,
    val foregroundColor: Color,
    val iconColor: Color
)
