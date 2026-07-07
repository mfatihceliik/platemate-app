package com.mefy.platemate.presentation.features.admin.accentcolors.form

import androidx.compose.runtime.Immutable

@Immutable
data class AccentColorFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val hex: String = "#",
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = HEX_REGEX.matches(hex) && sortOrder.toIntOrNull()?.let { it >= 0 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving

    private companion object {
        val HEX_REGEX = Regex("^#[0-9A-Fa-f]{6}$")
    }
}
