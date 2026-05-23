package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject

class SearchPlateUseCase @Inject constructor(
    private val repository: PlateRepository
) {
    suspend operator fun invoke(plateCode: String) =
        repository.searchPlate(normalizePlateCode(plateCode))

    private fun normalizePlateCode(input: String): String =
        input.trim()
            .replace(" ", "")
            .uppercase()
}
