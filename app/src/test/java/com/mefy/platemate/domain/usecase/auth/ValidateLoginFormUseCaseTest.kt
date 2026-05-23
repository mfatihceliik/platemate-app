package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.model.auth.PasswordValidationReason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateLoginFormUseCaseTest {

    private val useCase = ValidateLoginFormUseCase(ValidateEmailFormatUseCase())

    @Test
    fun emptyEmailAndPassword_returnsBothEmptyAndDisabledSubmit() {
        val result = useCase(email = "", password = "")

        assertEquals(EmailValidationReason.EMPTY, result.emailReason)
        assertEquals(PasswordValidationReason.EMPTY, result.passwordReason)
        assertFalse(result.isSubmitEnabled)
    }

    @Test
    fun invalidEmailWithFilledPassword_returnsInvalidEmailAndDisabledSubmit() {
        val result = useCase(email = "invalid-email", password = "123456")

        assertEquals(EmailValidationReason.INVALID_FORMAT, result.emailReason)
        assertEquals(PasswordValidationReason.NONE, result.passwordReason)
        assertFalse(result.isSubmitEnabled)
    }

    @Test
    fun validEmailWithFilledPassword_returnsEnabledSubmit() {
        val result = useCase(email = "user@example.com", password = "123456")

        assertEquals(EmailValidationReason.NONE, result.emailReason)
        assertEquals(PasswordValidationReason.NONE, result.passwordReason)
        assertTrue(result.isSubmitEnabled)
    }
}
