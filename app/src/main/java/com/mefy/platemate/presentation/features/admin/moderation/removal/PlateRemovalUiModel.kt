package com.mefy.platemate.presentation.features.admin.moderation.removal

import androidx.compose.runtime.Immutable

@Immutable
data class PlateRemovalUiModel(
    val id: Long,
    val plateCode: String,
    val requesterUsername: String,
    val requesterEmail: String,
    val reasonCode: String,
    val description: String,
    val date: String
)