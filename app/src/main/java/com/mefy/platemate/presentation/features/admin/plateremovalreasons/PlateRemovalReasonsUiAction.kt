package com.mefy.platemate.presentation.features.admin.plateremovalreasons

sealed interface PlateRemovalReasonsUiAction {
    data object BackClicked : PlateRemovalReasonsUiAction
    data object RetryClicked : PlateRemovalReasonsUiAction
    data object AddClicked : PlateRemovalReasonsUiAction
    data class EditClicked(val id: Long) : PlateRemovalReasonsUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : PlateRemovalReasonsUiAction
}

