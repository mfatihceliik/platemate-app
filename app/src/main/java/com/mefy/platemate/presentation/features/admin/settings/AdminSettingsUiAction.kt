package com.mefy.platemate.presentation.features.admin.settings

sealed interface AdminSettingsUiAction {
    data object BackClicked : AdminSettingsUiAction
    data object RetryClicked : AdminSettingsUiAction
    data object SaveClicked : AdminSettingsUiAction
    data class FollowLimitChanged(val value: String) : AdminSettingsUiAction
    data class AlarmLimitChanged(val value: String) : AdminSettingsUiAction
    data class MessageLimitChanged(val value: String) : AdminSettingsUiAction
    data class ReportThresholdChanged(val value: String) : AdminSettingsUiAction
}