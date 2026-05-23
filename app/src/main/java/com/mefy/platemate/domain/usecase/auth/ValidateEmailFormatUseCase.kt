package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import javax.inject.Inject

class ValidateEmailFormatUseCase @Inject constructor() {

    private companion object {
        val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }

    operator fun invoke(email: String): EmailValidationReason {
        return when {
            email.isBlank() -> EmailValidationReason.EMPTY
            EMAIL_REGEX.matches(email) -> EmailValidationReason.NONE
            else -> EmailValidationReason.INVALID_FORMAT
        }
    }
}
