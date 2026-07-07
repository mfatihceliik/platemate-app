package com.mefy.platemate.presentation.features.admin.premiumfeatures

sealed interface PremiumFeaturesUiAction {
    data object BackClicked : PremiumFeaturesUiAction
    data object RetryClicked : PremiumFeaturesUiAction
    data object AddClicked : PremiumFeaturesUiAction
    data class EditClicked(val id: Long) : PremiumFeaturesUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : PremiumFeaturesUiAction
}
