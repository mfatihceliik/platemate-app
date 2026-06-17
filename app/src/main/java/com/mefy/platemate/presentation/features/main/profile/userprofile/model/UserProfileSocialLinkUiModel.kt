package com.mefy.platemate.presentation.features.main.profile.userprofile.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfileSocialLinkUiModel(
    val platform: String,   // "INSTAGRAM", "X", "FACEBOOK", "WEBSITE"
    val url: String
)
