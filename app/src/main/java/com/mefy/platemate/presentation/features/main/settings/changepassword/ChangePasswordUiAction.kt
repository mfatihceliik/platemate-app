package com.mefy.platemate.presentation.features.main.settings.changepassword

sealed interface ChangePasswordUiAction {
    data class CurrentPasswordChanged(val value: String) : ChangePasswordUiAction
    data class NewPasswordChanged(val value: String) : ChangePasswordUiAction
    data class ConfirmPasswordChanged(val value: String) : ChangePasswordUiAction
    data object SubmitClicked : ChangePasswordUiAction
    data object BackClicked : ChangePasswordUiAction
}
