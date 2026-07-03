package com.mefy.platemate.presentation.features.main.settings.sociallinks

sealed interface SocialLinksUiEffect {
    data object NavigateBack : SocialLinksUiEffect
}
