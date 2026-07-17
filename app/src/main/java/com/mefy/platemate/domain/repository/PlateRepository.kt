package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult

interface PlateRepository {
    suspend fun searchPlate(plateCode: String): AppResult<PlateSearchResult>
    suspend fun followPlate(plateCode: String): AppResult<Unit>
    suspend fun unfollowPlate(plateCode: String): AppResult<Unit>
    suspend fun createRemovalRequest(plateId: Long, reasonCode: String, description: String): AppResult<Unit>
    suspend fun getPlateRemovalReasons(): AppResult<List<com.mefy.platemate.domain.model.plate.PlateRemovalReason>>
}
