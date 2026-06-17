package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

sealed interface SocialLinksUiAction {
    data class ExistingUrlChanged(val id: Long, val url: String) : SocialLinksUiAction
    data class UpdateClicked(val id: Long) : SocialLinksUiAction
    data class DeleteClicked(val id: Long) : SocialLinksUiAction
    data class NewPlatformSelected(val platform: String) : SocialLinksUiAction
    data class NewUrlChanged(val url: String) : SocialLinksUiAction
    data object AddClicked : SocialLinksUiAction
}
