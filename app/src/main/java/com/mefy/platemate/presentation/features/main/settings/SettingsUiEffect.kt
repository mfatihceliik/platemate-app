package com.mefy.platemate.presentation.features.main.settings

sealed interface SettingsUiEffect {
    data object NavigateToChangePassword : SettingsUiEffect
    data object NavigateToEditProfile : SettingsUiEffect
    data object NavigateToPremium : SettingsUiEffect
    data object NavigateToThemeColor : SettingsUiEffect
    data object NavigateToCardStyle : SettingsUiEffect
    data object NavigateToLanguage : SettingsUiEffect
    data object NavigateToNotificationPreferences : SettingsUiEffect
    data object NavigateToAdmin : SettingsUiEffect
}
