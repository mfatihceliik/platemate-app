package com.mefy.platemate.presentation.features.auth.login

sealed interface LoginUiAction {
    data class EmailChanged(val value: String) : LoginUiAction
    data class PasswordChanged(val value: String) : LoginUiAction
    data class PrefillEmailReceived(val value: String) : LoginUiAction
    data object SubmitClicked : LoginUiAction
}
