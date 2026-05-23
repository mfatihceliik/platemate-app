package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidateEmailFormatUseCaseTest {

    private val useCase = ValidateEmailFormatUseCase()

    @Test
    fun emptyEmail_returnsEmptyReason() {
        val result = useCase("")

        assertEquals(EmailValidationReason.EMPTY, result)
    }

    @Test
    fun invalidEmail_returnsInvalidFormatReason() {
        val result = useCase("not-an-email")

        assertEquals(EmailValidationReason.INVALID_FORMAT, result)
    }

    @Test
    fun validEmail_returnsNoneReason() {
        val result = useCase("user@example.com")

        assertEquals(EmailValidationReason.NONE, result)
    }
}
