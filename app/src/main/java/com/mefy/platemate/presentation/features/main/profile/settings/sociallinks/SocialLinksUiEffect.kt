package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

import com.mefy.platemate.presentation.common.text.UiText

sealed interface SocialLinksUiEffect {
    data class ShowSnackbar(val message: UiText) : SocialLinksUiEffect
}
