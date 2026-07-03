package com.mefy.platemate.presentation.features.main.settings.sociallinks

sealed interface SocialLinksUiAction {
    data object BackClicked : SocialLinksUiAction
    data object RetryClicked : SocialLinksUiAction
    data class PlatformSelected(val platformId: String) : SocialLinksUiAction
    data class UrlChanged(val value: String) : SocialLinksUiAction
    data object AddClicked : SocialLinksUiAction
    data class DeleteClicked(val linkId: Long) : SocialLinksUiAction
}
