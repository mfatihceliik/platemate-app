package com.mefy.platemate.presentation.features.auth.register

sealed interface RegisterUiEffect {
    data object NavigateAfterRegister : RegisterUiEffect
}
