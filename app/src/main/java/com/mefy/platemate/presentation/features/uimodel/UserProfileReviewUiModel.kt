package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfileReviewUiModel(
    val id: Long,
    val plateCode: String,        // "34"
    val plateNumber: String,      // "34 EK 0682"
    val city: String,
    val date: String,
    val rating: Float,
    val tags: List<String>,
    val comment: String
)
