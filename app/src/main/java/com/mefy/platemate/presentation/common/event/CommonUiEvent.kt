package com.mefy.platemate.presentation.common.event

import com.mefy.platemate.presentation.common.text.UiText

sealed interface CommonUiEvent {
    data class ShowSnackbar(val message: UiText) : CommonUiEvent
    data class ShowDialog(val dialog: CommonDialogModel) : CommonUiEvent
}
