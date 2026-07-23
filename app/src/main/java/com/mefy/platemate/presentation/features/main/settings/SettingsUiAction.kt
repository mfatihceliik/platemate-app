package com.mefy.platemate.presentation.features.main.settings

sealed interface SettingsUiAction {
    data object ChangePasswordClicked : SettingsUiAction
    data object EditProfileClicked : SettingsUiAction
    data object PremiumClicked : SettingsUiAction
    data object ThemeColorClicked : SettingsUiAction
    data object CardStyleClicked : SettingsUiAction
    data object LanguageClicked : SettingsUiAction
    data object NotificationPreferencesClicked : SettingsUiAction
    data object AdminPanelClicked : SettingsUiAction
    data object SignOutClicked : SettingsUiAction
}
