package com.mefy.platemate.presentation.features.admin.premiumplans.form

sealed interface PremiumPlanFormUiEffect {
    data object NavigateBack : PremiumPlanFormUiEffect
}
