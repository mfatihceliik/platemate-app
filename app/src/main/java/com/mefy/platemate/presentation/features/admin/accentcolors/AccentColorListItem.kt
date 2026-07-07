package com.mefy.platemate.presentation.features.admin.accentcolors

import androidx.compose.runtime.Immutable

@Immutable
data class AccentColorListItem(
    val id: Long,
    val hex: String,
    val sortOrder: Int,
    val active: Boolean
)
