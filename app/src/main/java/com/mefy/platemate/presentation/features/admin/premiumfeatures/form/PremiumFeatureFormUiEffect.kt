package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

sealed interface PremiumFeatureFormUiEffect {
    data object NavigateBack : PremiumFeatureFormUiEffect
}
