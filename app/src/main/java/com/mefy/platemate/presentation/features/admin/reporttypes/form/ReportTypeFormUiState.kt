package com.mefy.platemate.presentation.features.admin.reporttypes.form

import androidx.compose.runtime.Immutable

@Immutable
data class ReportTypeFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val code: String = "",
    val label: String = "",
    val description: String = "",
    val iconKey: String = "",
    val severityCode: String = "RED",
    val colorHex: String = "#",
    val weight: String = "",
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = code.isNotBlank() && label.isNotBlank() && description.isNotBlank() &&
                iconKey.isNotBlank() && severityCode.isNotBlank() &&
                COLOR_REGEX.matches(colorHex) &&
                weight.toIntOrNull()?.let { it >= 1 } == true &&
                sortOrder.toIntOrNull()?.let { it >= 1 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving

    private companion object {
        val COLOR_REGEX = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")
    }
}
