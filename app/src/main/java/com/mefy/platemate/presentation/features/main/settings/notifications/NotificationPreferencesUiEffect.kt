package com.mefy.platemate.presentation.features.main.settings.notifications

import com.mefy.platemate.presentation.common.text.UiText

sealed interface NotificationPreferencesUiEffect {
    data class ShowSnackbar(val message: UiText) : NotificationPreferencesUiEffect
}
