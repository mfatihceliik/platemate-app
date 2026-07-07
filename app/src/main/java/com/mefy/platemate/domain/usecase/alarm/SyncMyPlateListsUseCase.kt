package com.mefy.platemate.domain.usecase.alarm

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.remote.dto.plate.MyPlateListsDto
import com.mefy.platemate.data.remote.dto.plate.PlateListItemDto
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.AlarmPlateRepository
import com.mefy.platemate.domain.repository.SavedPlateRepository
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import javax.inject.Inject

/**
 * Single round-trip on SearchScreen open: pulls the user's saved + alarm plates from the
 * backend and replaces both local caches. Backend is the source of truth.
 */
class SyncMyPlateListsUseCase @Inject constructor(
    private val plateApiService: PlateApiService,
    private val savedPlateRepository: SavedPlateRepository,
    private val alarmPlateRepository: AlarmPlateRepository,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase
) {
    suspend operator fun invoke(): AppResult<Unit> {
        val result = safeApiCall { plateApiService.getMyLists() }
        return when (result) {
            is AppResult.Success -> {
                val dto: MyPlateListsDto = result.data
                val base = System.currentTimeMillis()
                savedPlateRepository.replaceFromRemote(
                    dto.savedPlates.orEmpty().mapIndexed { index, item -> item.toSavedPlate(base - index) }
                )
                alarmPlateRepository.replaceFromRemote(
                    dto.alarmPlates.orEmpty().mapIndexed { index, item -> item.toAlarmPlate(base - index) }
                )
                AppResult.Success(Unit)
            }
            is AppResult.Error -> result
        }
    }

    private fun PlateListItemDto.toSavedPlate(orderedSavedAt: Long): SavedPlate {
        val code = plateCode.orEmpty()
        return SavedPlate(
            normalizedPlateCode = code,
            formattedPlateCode = formatTurkishPlateInputUseCase(code),
            cityName = cityName,
            ratingAverage = ratingAverage ?: 0.0,
            commentCount = reviewCount ?: 0L,
            reportTypes = emptyList(),
            savedAt = orderedSavedAt
        )
    }

    private fun PlateListItemDto.toAlarmPlate(orderedSavedAt: Long): AlarmPlate {
        val code = plateCode.orEmpty()
        return AlarmPlate(
            normalizedPlateCode = code,
            formattedPlateCode = formatTurkishPlateInputUseCase(code),
            cityName = cityName,
            ratingAverage = ratingAverage ?: 0.0,
            commentCount = reviewCount ?: 0L,
            reportTypes = emptyList(),
            savedAt = orderedSavedAt
        )
    }
}
