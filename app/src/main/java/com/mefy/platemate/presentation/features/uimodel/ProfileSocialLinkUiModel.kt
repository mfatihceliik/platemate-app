package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ProfileSocialLinkUiModel(
    val id: Long?,
    val platform: String,
    val url: String,
    val iconUrl: String?,
    val backgroundColor: Color,
    val iconTint: Color
)
