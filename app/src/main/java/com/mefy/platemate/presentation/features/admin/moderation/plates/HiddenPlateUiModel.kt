package com.mefy.platemate.presentation.features.admin.moderation.plates

import androidx.compose.runtime.Immutable

@Immutable
data class HiddenPlateUiModel(
    val id: Long,
    val plateCode: String,
    val statusCode: String,
    val hiddenReason: String,
    val reportCount: Int
)