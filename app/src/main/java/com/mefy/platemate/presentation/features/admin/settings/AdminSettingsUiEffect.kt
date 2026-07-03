package com.mefy.platemate.presentation.features.admin.settings

sealed interface AdminSettingsUiEffect {
    data object NavigateBack : AdminSettingsUiEffect
}