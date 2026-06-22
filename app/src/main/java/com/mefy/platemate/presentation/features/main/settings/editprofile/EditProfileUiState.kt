package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
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
