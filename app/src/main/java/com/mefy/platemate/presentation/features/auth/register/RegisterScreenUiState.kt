package com.mefy.platemate.presentation.features.auth.register

import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText

data class RegisterScreenUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isEmailFormatValid: Boolean = true,
    val isPasswordLengthValid: Boolean = true,
    val hasSubmittedOnce: Boolean = false,
    val passwordMinLength: Int = 0,
    val passwordStrength: PasswordStrength = PasswordStrength(),
    val isSubmitEnabled: Boolean = false,
    val submitState: UiActionState = UiActionState.Idle,
    val formMessage: UiText? = null
)
