package com.mefy.platemate.presentation.features.admin.moderation.removal

sealed interface PlateRemovalUiAction {
    data object BackClicked : PlateRemovalUiAction
    data object RetryClicked : PlateRemovalUiAction
    data object LoadMore : PlateRemovalUiAction
    data class AcceptClicked(val requestId: Long) : PlateRemovalUiAction
    data class RejectClicked(val requestId: Long) : PlateRemovalUiAction
}