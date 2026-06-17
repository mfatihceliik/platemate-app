package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileStatusSummaryUiModel(
    val approved: Long = 0L,
    val pendingReview: Long = 0L,
    val rejected: Long = 0L,
    val removedByUser: Long = 0L,
    val removedByModerator: Long = 0L,
    val removedByLegalRequest: Long = 0L
)
