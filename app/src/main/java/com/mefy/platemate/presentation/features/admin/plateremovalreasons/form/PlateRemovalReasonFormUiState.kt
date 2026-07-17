package com.mefy.platemate.presentation.features.admin.plateremovalreasons.form

import androidx.compose.runtime.Immutable

@Immutable
data class PlateRemovalReasonFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val code: String = "",
    val label: String = "",
    val requiresDescription: Boolean = false,
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = code.isNotBlank() &&
            label.isNotBlank() &&
            sortOrder.toIntOrNull()?.let { it >= 1 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving
}

