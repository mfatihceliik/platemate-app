package com.mefy.platemate.presentation.features.admin.socialplatforms.form

sealed interface SocialPlatformFormUiEffect {
    data object NavigateBack : SocialPlatformFormUiEffect
}
