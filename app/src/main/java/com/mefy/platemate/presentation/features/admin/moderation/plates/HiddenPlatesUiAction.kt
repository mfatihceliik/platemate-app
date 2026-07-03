package com.mefy.platemate.presentation.features.admin.moderation.plates

sealed interface HiddenPlatesUiAction {
    data object BackClicked : HiddenPlatesUiAction
    data object RetryClicked : HiddenPlatesUiAction
    data object LoadMore : HiddenPlatesUiAction
    data class RestoreClicked(val plateId: Long) : HiddenPlatesUiAction
}