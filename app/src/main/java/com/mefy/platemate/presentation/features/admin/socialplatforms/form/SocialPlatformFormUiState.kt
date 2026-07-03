package com.mefy.platemate.presentation.features.admin.socialplatforms.form

import androidx.compose.runtime.Immutable

@Immutable
data class SocialPlatformFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val code: String = "",
    val label: String = "",
    val iconUrl: String = "",
    val backgroundColorHex: String = "",
    val iconTintColorHex: String = "",
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = code.isNotBlank() && label.isNotBlank() &&
                (backgroundColorHex.isBlank() || COLOR_REGEX.matches(backgroundColorHex)) &&
                (iconTintColorHex.isBlank() || COLOR_REGEX.matches(iconTintColorHex)) &&
                sortOrder.toIntOrNull()?.let { it >= 1 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving

    private companion object {
        val COLOR_REGEX = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    }
}
