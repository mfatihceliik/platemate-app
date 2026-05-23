package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatePasswordStrengthUseCaseTest {

    private val useCase = CalculateRegisterPasswordStrengthUseCase()

    @Test
    fun emptyPassword_returnsNoneAndZeroProgress() {
        val result = useCase("")

        assertEquals(PasswordStrengthLevel.NONE, result.level)
        assertEquals(0f, result.progress)
        assertFalse(result.meetsMinLength)
        assertEquals(6, result.minLength)
    }

    @Test
    fun shortSimplePassword_returnsWeakAndFailsMinLength() {
        val result = useCase("abc")

        assertEquals(PasswordStrengthLevel.WEAK, result.level)
        assertFalse(result.meetsMinLength)
        assertEquals(6, result.minLength)
    }

    @Test
    fun mixedPassword_returnsMediumAndPassesMinLength() {
        val result = useCase("Abcd12")

        assertEquals(PasswordStrengthLevel.MEDIUM, result.level)
        assertTrue(result.meetsMinLength)
        assertEquals(6, result.minLength)
    }

    @Test
    fun strongPassword_returnsStrong() {
        val result = useCase("Strong123!")

        assertEquals(PasswordStrengthLevel.STRONG, result.level)
        assertTrue(result.meetsMinLength)
        assertEquals(6, result.minLength)
    }
}
