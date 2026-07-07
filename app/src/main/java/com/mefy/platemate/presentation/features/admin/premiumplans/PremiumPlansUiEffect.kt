package com.mefy.platemate.presentation.features.admin.premiumplans

sealed interface PremiumPlansUiEffect {
    data object NavigateBack : PremiumPlansUiEffect
    data class NavigateToForm(val planId: Long) : PremiumPlansUiEffect
}
