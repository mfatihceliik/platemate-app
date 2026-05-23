package com.mefy.platemate.domain.usecase.search

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateTurkishPlateUseCaseTest {

    private val useCase = ValidateTurkishPlateUseCase()

    @Test
    fun returnsTrueForValidTurkishPlates() {
        val validPlates = listOf(
            "34 A 1234",
            "34 A 12345",
            "34 AB 123",
            "34 AB 1234",
            "34 ABC 12",
            "34 ABC 123"
        )

        validPlates.forEach { plate ->
            assertTrue("Expected valid plate: $plate", useCase(plate))
        }
    }

    @Test
    fun returnsFalseForInvalidTurkishPlates() {
        val invalidPlates = listOf(
            "00 AB 123",
            "82 AB 123",
            "34 ABCD 12",
            "34 A 123",
            "6 ABC 123"
        )

        invalidPlates.forEach { plate ->
            assertFalse("Expected invalid plate: $plate", useCase(plate))
        }
    }

    @Test
    fun normalizesInputByRemovingSpacesAndUppercasing() {
        assertTrue(useCase("34 ab 123"))
        assertTrue(useCase("34   aBc   123"))
    }
}
