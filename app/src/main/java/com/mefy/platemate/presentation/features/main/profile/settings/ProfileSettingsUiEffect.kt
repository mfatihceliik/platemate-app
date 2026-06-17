package com.mefy.platemate.presentation.features.main.profile.settings

sealed interface ProfileSettingsUiEffect {
    data object NavigateToChangePassword : ProfileSettingsUiEffect
    data object NavigateToEditProfile : ProfileSettingsUiEffect
    data object NavigateToPremium : ProfileSettingsUiEffect
    data object NavigateToThemeColor : ProfileSettingsUiEffect
    data object NavigateToLanguage : ProfileSettingsUiEffect
    data object NavigateToNotificationPreferences : ProfileSettingsUiEffect
    data object NavigateToSocialLinks : ProfileSettingsUiEffect
}
