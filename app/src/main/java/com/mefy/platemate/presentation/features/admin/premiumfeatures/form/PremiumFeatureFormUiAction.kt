package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

sealed interface PremiumFeatureFormUiAction {
    data object BackClicked : PremiumFeatureFormUiAction
    data object SaveClicked : PremiumFeatureFormUiAction
    data class IconKeyChanged(val value: String) : PremiumFeatureFormUiAction
    data class TitleChanged(val locale: String, val value: String) : PremiumFeatureFormUiAction
    data class SubtitleChanged(val locale: String, val value: String) : PremiumFeatureFormUiAction
    data class AddLanguage(val locale: String) : PremiumFeatureFormUiAction
    data class SortOrderChanged(val value: String) : PremiumFeatureFormUiAction
}
