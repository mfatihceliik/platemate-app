package com.mefy.platemate.domain.usecase.search

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatTurkishPlateInputUseCaseTest {

    private val useCase = FormatTurkishPlateInputUseCase()

    @Test
    fun formatsCompactInputIntoPlateGroups() {
        val result = useCase("34abc123")

        assertEquals("34 ABC 123", result)
    }

    @Test
    fun autoPadsSingleDigitCityWhenLetterStarts() {
        val result = useCase("1abc1234")

        assertEquals("01 ABC 1234", result)
    }

    @Test
    fun removesInvalidCharactersAndNormalizesWhitespace() {
        val result = useCase(" 34   a-b_c?123 ")

        assertEquals("34 ABC 123", result)
    }

    @Test
    fun limitsFormattedLengthToTwelveCharacters() {
        val result = useCase("34abc123456789")

        assertEquals("34 ABC 12345", result)
    }
}
