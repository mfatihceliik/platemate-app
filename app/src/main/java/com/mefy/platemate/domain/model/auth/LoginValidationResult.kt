package com.mefy.platemate.domain.model.auth

data class LoginValidationResult(
    val emailReason: EmailValidationReason = EmailValidationReason.NONE,
    val passwordReason: PasswordValidationReason = PasswordValidationReason.NONE,
    val isSubmitEnabled: Boolean = false
)
