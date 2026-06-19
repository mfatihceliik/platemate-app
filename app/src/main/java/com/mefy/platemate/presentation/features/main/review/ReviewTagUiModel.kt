package com.mefy.platemate.presentation.features.main.review

import androidx.compose.runtime.Immutable

@Immutable
data class ReviewTagUiModel(
    val code: String,
    val label: String,
    val isSelected: Boolean = false
)
