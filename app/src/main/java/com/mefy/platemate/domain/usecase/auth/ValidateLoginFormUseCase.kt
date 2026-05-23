package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.model.auth.LoginValidationResult
import com.mefy.platemate.domain.model.auth.PasswordValidationReason
import javax.inject.Inject

class ValidateLoginFormUseCase @Inject constructor(
    private val validateEmailFormatUseCase: ValidateEmailFormatUseCase
) {

    operator fun invoke(email: String, password: String): LoginValidationResult {
        val emailReason = validateEmailFormatUseCase(email)
        val passwordReason = if (password.isBlank()) {
            PasswordValidationReason.EMPTY
        } else {
            PasswordValidationReason.NONE
        }

        return LoginValidationResult(
            emailReason = emailReason,
            passwordReason = passwordReason,
            isSubmitEnabled = emailReason == EmailValidationReason.NONE &&
                passwordReason == PasswordValidationReason.NONE
        )
    }
}
