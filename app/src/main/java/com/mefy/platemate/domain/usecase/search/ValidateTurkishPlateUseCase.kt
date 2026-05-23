package com.mefy.platemate.domain.usecase.search

import javax.inject.Inject

class ValidateTurkishPlateUseCase @Inject constructor() {

    fun normalize(plateCode: String): String =
        plateCode.replace(" ", "").uppercase()

    operator fun invoke(plateCode: String): Boolean {
        val normalizedPlate = normalize(plateCode)
        if (normalizedPlate.isBlank()) {
            return false
        }
        return TURKISH_PLATE_REGEX.matches(normalizedPlate)
    }

    private companion object {
        val TURKISH_PLATE_REGEX = Regex(
            pattern = "^(0[1-9]|[1-7][0-9]|8[0-1])(([A-Z])(\\d{4,5})|([A-Z]{2})(\\d{3,4})|([A-Z]{3})(\\d{2,3}))$"
        )
    }
}
