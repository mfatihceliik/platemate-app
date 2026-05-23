package com.mefy.platemate.presentation.features.auth.register

sealed interface RegisterUiAction {
    data class UsernameChanged(val value: String) : RegisterUiAction
    data class EmailChanged(val value: String) : RegisterUiAction
    data class PasswordChanged(val value: String) : RegisterUiAction
    data class PrefillIdentifierReceived(val value: String) : RegisterUiAction
    data object SubmitClicked : RegisterUiAction
}
