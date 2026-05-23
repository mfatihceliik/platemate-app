package com.mefy.platemate.domain.usecase.search

import javax.inject.Inject

class FormatTurkishPlateInputUseCase @Inject constructor() {

    operator fun invoke(rawInput: String): String {
        if (rawInput.isBlank()) {
            return ""
        }

        val compact = rawInput.uppercase()
            .filter { char -> char.isAsciiUpperLetterOrDigit() }
        if (compact.isEmpty()) {
            return ""
        }

        val cityAndRemainder = extractCityCodeAndRemainder(compact)
        val letters = cityAndRemainder.remainder.takeWhile { it.isAsciiUpperLetter() }.take(MAX_LETTER_COUNT)
        val remainingAfterLetters = cityAndRemainder.remainder.drop(letters.length)
        val numbers = remainingAfterLetters.takeWhile { it.isDigit() }.take(MAX_NUMBER_COUNT)

        return listOf(cityAndRemainder.cityCode, letters, numbers)
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")
            .take(MAX_FORMATTED_LENGTH)
    }

    private fun extractCityCodeAndRemainder(input: String): CityCodeAndRemainder {
        val first = input.first()
        if (!first.isDigit()) {
            return CityCodeAndRemainder(cityCode = "", remainder = input)
        }

        if (input.length == 1) {
            return CityCodeAndRemainder(cityCode = first.toString(), remainder = "")
        }

        val second = input[1]
        if (second.isDigit()) {
            return CityCodeAndRemainder(
                cityCode = "${first}${second}",
                remainder = input.drop(2)
            )
        }

        if (second.isAsciiUpperLetter()) {
            return CityCodeAndRemainder(
                cityCode = "0$first",
                remainder = input.drop(1)
            )
        }

        return CityCodeAndRemainder(cityCode = first.toString(), remainder = input.drop(1))
    }

    private fun Char.isAsciiUpperLetterOrDigit(): Boolean = isAsciiUpperLetter() || isDigit()

    private fun Char.isAsciiUpperLetter(): Boolean = this in 'A'..'Z'

    private data class CityCodeAndRemainder(
        val cityCode: String,
        val remainder: String
    )

    private companion object {
        const val MAX_LETTER_COUNT = 3
        const val MAX_NUMBER_COUNT = 5
        const val MAX_FORMATTED_LENGTH = 12
    }
}
