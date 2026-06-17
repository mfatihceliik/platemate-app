package com.mefy.platemate.presentation.features.main.profile.settings.editprofile

import androidx.compose.runtime.Immutable

@Immutable
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val displayName: String = "",
    val username: String = "",
    val bio: String = "",
    val instagramUrl: String = "",
    val twitterUrl: String = "",
    val linkedInUrl: String = "",
    val websiteUrl: String = "",
    val displayNameError: String? = null,
    val usernameError: String? = null
) {
    val bioLength: Int get() = bio.length
    val bioMaxLength: Int get() = 160
}
