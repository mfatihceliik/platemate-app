package com.mefy.platemate.presentation.features.admin.plateremovalreasons

import androidx.compose.runtime.Immutable

@Immutable
data class PlateRemovalReasonListItem(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val active: Boolean
)

