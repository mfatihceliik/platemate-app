package com.mefy.platemate.presentation.features.main.settings.changepassword

sealed interface ChangePasswordUiEffect {
    data object NavigateBack : ChangePasswordUiEffect
    data object PasswordChanged : ChangePasswordUiEffect
}
