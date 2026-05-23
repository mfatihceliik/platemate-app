package com.mefy.platemate.presentation.common.state

sealed interface UiActionState {
    data object Idle : UiActionState
    data object Loading : UiActionState
    data object Error : UiActionState
}
