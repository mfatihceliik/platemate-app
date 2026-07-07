package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.ui.graphics.Color

data class SocialPlatform(
    val id: Long,
    val code: String,
    val displayName: String,
    val iconUrl: String?,
    val baseUrl: String?,
    val backgroundColor: Color,
    val iconTint: Color
)
