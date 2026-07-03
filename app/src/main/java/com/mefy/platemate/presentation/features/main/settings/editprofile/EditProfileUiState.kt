package com.mefy.platemate.presentation.features.main.settings.editprofile

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatform

@Immutable
data class EditProfileUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val isSaving: Boolean = false,
    val displayName: String = "",
    val username: String = "",
    val bio: String = "",
    val profilePhotoUrl: String = "",
    val showAvatarDialog: Boolean = false,
    val avatarUrlDraft: String = "",
    val displayNameError: UiText? = null,
    val usernameError: UiText? = null,
    val availablePlatforms: List<SocialPlatform> = emptyList(),
    val socialLinks: SnapshotStateMap<String, String> = mutableStateMapOf()
) {

    val bioLength: Int get() = bio.length
    val bioMaxLength: Int get() = 160
}
