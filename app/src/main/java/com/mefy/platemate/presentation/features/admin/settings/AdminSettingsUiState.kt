package com.mefy.platemate.presentation.features.admin.settings

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class AdminSettingsUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: UiText? = null,
    val followLimit: String = "",
    val alarmLimit: String = "",
    val messageLimit: String = "",
    val reportThreshold: String = ""
) {
    val isSaveEnabled: Boolean
        get() = !isSaving &&
                followLimit.toIntOrNull()?.let { it >= 1 } == true &&
                alarmLimit.toIntOrNull()?.let { it >= 1 } == true &&
                messageLimit.toIntOrNull()?.let { it >= 1 } == true &&
                reportThreshold.toIntOrNull()?.let { it >= 1 } == true
}