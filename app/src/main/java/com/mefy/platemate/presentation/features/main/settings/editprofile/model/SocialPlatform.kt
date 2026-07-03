package com.mefy.platemate.presentation.features.main.settings.editprofile.model

import androidx.compose.ui.graphics.Color

data class SocialPlatform(
    val id: Long,
    val code: String,
    val displayName: String,
    val iconUrl: String?,
    val backgroundColor: Color,
    val iconTint: Color
)
