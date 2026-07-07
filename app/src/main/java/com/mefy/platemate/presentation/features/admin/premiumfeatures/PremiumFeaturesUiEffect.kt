package com.mefy.platemate.presentation.features.admin.premiumfeatures

sealed interface PremiumFeaturesUiEffect {
    data object NavigateBack : PremiumFeaturesUiEffect
    data class NavigateToForm(val featureId: Long?) : PremiumFeaturesUiEffect
}
