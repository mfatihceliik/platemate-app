package com.mefy.platemate.presentation.features.admin.socialplatforms

import androidx.compose.runtime.Immutable

@Immutable
data class SocialPlatformListItem(
    val id: Long,
    val code: String,
    val label: String,
    val sortOrder: Int,
    val active: Boolean
)
