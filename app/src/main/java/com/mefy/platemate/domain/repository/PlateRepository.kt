package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult

interface PlateRepository {
    suspend fun searchPlate(plateCode: String): AppResult<PlateSearchResult>
}
