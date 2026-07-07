package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

import androidx.compose.runtime.Immutable

@Immutable
data class PremiumFeatureFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val iconKey: String = "check",
    val titles: Map<String, String> = mapOf("tr" to "", "en" to ""),
    val subtitles: Map<String, String> = mapOf("tr" to "", "en" to ""),
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = iconKey.isNotBlank() && titles["tr"]?.isNotBlank() == true && titles["en"]?.isNotBlank() == true &&
                sortOrder.toIntOrNull()?.let { it >= 0 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving
}
