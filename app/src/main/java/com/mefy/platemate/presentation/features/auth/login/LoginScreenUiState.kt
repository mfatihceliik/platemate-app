package com.mefy.platemate.presentation.features.auth.login

data class LoginScreenUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isEmailFormatValid: Boolean = true,
    val hasSubmittedOnce: Boolean = false,
    val isSubmitEnabled: Boolean = false,
    val isLoading: Boolean = false
)
