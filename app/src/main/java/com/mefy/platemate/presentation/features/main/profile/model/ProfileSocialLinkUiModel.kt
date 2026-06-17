package com.mefy.platemate.presentation.features.main.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileSocialLinkUiModel(
    val id: Long?,
    val platform: String,
    val url: String
)
