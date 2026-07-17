package com.mefy.platemate.presentation.features.main.platedetail.removal

import com.mefy.platemate.domain.model.plate.PlateRemovalReason

sealed interface PlateRemovalRequestUiAction {
    data object BackClicked : PlateRemovalRequestUiAction
    data class ReasonSelected(val reason: PlateRemovalReason) : PlateRemovalRequestUiAction
    data class DescriptionChanged(val value: String) : PlateRemovalRequestUiAction
    data object SubmitClicked : PlateRemovalRequestUiAction
}