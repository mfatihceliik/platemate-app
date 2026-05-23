package com.mefy.platemate.presentation.features.auth.login

sealed interface LoginUiEffect {
    data object NavigateAfterLogin : LoginUiEffect
}
