package com.mefy.platemate.presentation.features.admin.premiumplans.form

sealed interface PremiumPlanFormUiAction {
    data object BackClicked : PremiumPlanFormUiAction
    data object SaveClicked : PremiumPlanFormUiAction
    data class TitleChanged(val locale: String, val value: String) : PremiumPlanFormUiAction
    data class DescriptionChanged(val locale: String, val value: String) : PremiumPlanFormUiAction
    data class AddLanguage(val locale: String) : PremiumPlanFormUiAction
    data class AmountChanged(val value: String) : PremiumPlanFormUiAction
    data class CurrencyChanged(val value: String) : PremiumPlanFormUiAction
    data class DiscountChanged(val value: String) : PremiumPlanFormUiAction
    data class SortOrderChanged(val value: String) : PremiumPlanFormUiAction
}
