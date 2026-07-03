package com.mefy.platemate.presentation.features.main.settings.sociallinks

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatform

@Immutable
data class SocialLinksUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val links: List<ProfileSocialLinkUiModel> = emptyList(),
    val availablePlatforms: List<SocialPlatform> = emptyList(),
    // Yeni link ekleme formu.
    val selectedPlatformId: String? = null,
    val urlInput: String = "",
    val isSubmitting: Boolean = false
) {
    val isAddEnabled: Boolean
        get() = selectedPlatformId != null && urlInput.isNotBlank() && !isSubmitting
}
