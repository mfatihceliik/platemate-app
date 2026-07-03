package com.mefy.platemate.presentation.features.main.settings

sealed interface ProfileSettingsUiAction {
    data object ChangePasswordClicked : ProfileSettingsUiAction
    data object EditProfileClicked : ProfileSettingsUiAction
    data object PremiumClicked : ProfileSettingsUiAction
    data object ThemeColorClicked : ProfileSettingsUiAction
    data object LanguageClicked : ProfileSettingsUiAction
    data object NotificationPreferencesClicked : ProfileSettingsUiAction
    data object SocialLinksClicked : ProfileSettingsUiAction
    data object AdminPanelClicked : ProfileSettingsUiAction
    data object SignOutClicked : ProfileSettingsUiAction
    data object RetryClicked : ProfileSettingsUiAction
}
