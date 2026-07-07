package com.mefy.platemate.presentation.features.admin.premiumplans.form

import androidx.compose.runtime.Immutable

@Immutable
data class PremiumPlanFormUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val period: String = "",
    val titles: Map<String, String> = mapOf("tr" to "", "en" to ""),
    val descriptions: Map<String, String> = mapOf("tr" to "", "en" to ""),
    val amount: String = "",
    val currency: String = "TRY",
    val discountPercent: String = "",
    val sortOrder: String = ""
) {
    val isValid: Boolean
        get() = amount.toDoubleOrNull()?.let { it >= 0.0 } == true &&
                CURRENCY_REGEX.matches(currency) &&
                (discountPercent.isBlank() || discountPercent.toIntOrNull()?.let { it in 0..100 } == true) &&
                sortOrder.toIntOrNull()?.let { it >= 0 } == true

    val isSaveEnabled: Boolean get() = isValid && !isSaving

    private companion object {
        val CURRENCY_REGEX = Regex("^[A-Za-z]{3}$")
    }
}
