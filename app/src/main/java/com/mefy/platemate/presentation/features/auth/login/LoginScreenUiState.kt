package com.mefy.platemate.presentation.features.auth.login

import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText

data class LoginScreenUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isEmailFormatValid: Boolean = true,
    val hasSubmittedOnce: Boolean = false,
    val isSubmitEnabled: Boolean = false,
    val submitState: UiActionState = UiActionState.Idle,
    val formMessage: UiText? = null
)
