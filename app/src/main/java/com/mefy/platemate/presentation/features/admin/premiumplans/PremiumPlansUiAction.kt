package com.mefy.platemate.presentation.features.admin.premiumplans

sealed interface PremiumPlansUiAction {
    data object BackClicked : PremiumPlansUiAction
    data object RetryClicked : PremiumPlansUiAction
    data class EditClicked(val id: Long) : PremiumPlansUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : PremiumPlansUiAction
}
