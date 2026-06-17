package com.mefy.platemate.presentation.features.main.profile.settings

sealed interface ProfileSettingsUiAction {
    data object ChangePasswordClicked : ProfileSettingsUiAction
    data object EditProfileClicked : ProfileSettingsUiAction
    data object PremiumClicked : ProfileSettingsUiAction
    data object ThemeColorClicked : ProfileSettingsUiAction
    data object LanguageClicked : ProfileSettingsUiAction
    data object NotificationPreferencesClicked : ProfileSettingsUiAction
    data object SocialLinksClicked : ProfileSettingsUiAction
    data object SignOutClicked : ProfileSettingsUiAction
}
