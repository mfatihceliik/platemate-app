package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

import androidx.compose.runtime.Immutable

@Immutable
data class SocialLinksUiState(
    val isLoading: Boolean = true,
    val links: List<SocialLinkEditorUiModel> = emptyList(),
    val selectedPlatform: String = DEFAULT_PLATFORM,
    val newUrl: String = "",
    val isSubmitting: Boolean = false
) {
    companion object {
        const val DEFAULT_PLATFORM = "INSTAGRAM"
    }
}

@Immutable
data class SocialLinkEditorUiModel(
    val id: Long,
    val platform: String,
    val url: String,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false
)
