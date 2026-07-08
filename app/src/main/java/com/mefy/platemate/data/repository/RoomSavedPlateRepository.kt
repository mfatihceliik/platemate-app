package com.mefy.platemate.data.repository

import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.room.dao.SavedPlateDao
import com.mefy.platemate.data.local.room.entity.SavedPlateEntity
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.SavedPlateRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class RoomSavedPlateRepository @Inject constructor(
    private val savedPlateDao: SavedPlateDao,
    private val plateApiService: PlateApiService,
    private val sessionStore: SessionStore
) : SavedPlateRepository {

    override fun observeSavedPlates(): Flow<List<SavedPlate>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            savedPlateDao.observeSavedPlates(userId = userId)
                .map { entities -> entities.map(::mapToDomain) }
        }

    override fun observeSavedPlateCodes(): Flow<Set<String>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptySet())
            savedPlateDao.observeSavedPlateCodes(userId = userId)
                .map { codes -> codes.toSet() }
        }

    override suspend fun toggleSaved(plate: SavedPlate): Boolean {
        val userId = sessionStore.session.first()?.userId ?: return false
        val deletedCount = savedPlateDao.deleteByNormalizedPlate(
            userId = userId,
            normalizedPlateCode = plate.normalizedPlateCode
        )

        // Optimistic local toggle, then mirror to backend; revert local on failure.
        return if (deletedCount > 0) {
            val remote = safeResultCall { plateApiService.unsavePlate(plate.normalizedPlateCode) }
            if (remote is AppResult.Error) {
                savedPlateDao.upsert(mapToEntity(userId = userId, plate = plate))
                true
            } else {
                false
            }
        } else {
            savedPlateDao.upsert(mapToEntity(userId = userId, plate = plate))
            val remote = safeResultCall { plateApiService.savePlate(plate.normalizedPlateCode) }
            if (remote is AppResult.Error) {
                savedPlateDao.deleteByNormalizedPlate(userId, plate.normalizedPlateCode)
                false
            } else {
                true
            }
        }
    }

    override suspend fun replaceFromRemote(plates: List<SavedPlate>) {
        val userId = sessionStore.session.first()?.userId ?: return
        val entities = plates.map { mapToEntity(userId = userId, plate = it) }
        savedPlateDao.replaceAll(userId, entities)
    }

    private fun mapToDomain(entity: SavedPlateEntity): SavedPlate = SavedPlate(
        normalizedPlateCode = entity.normalizedPlateCode,
        formattedPlateCode = entity.formattedPlateCode,
        cityName = entity.cityName,
        ratingAverage = entity.ratingAverage,
        commentCount = entity.commentCount,
        reportTypes = entity.reportTypes.map(::mapReportTypeToDomain),
        savedAt = entity.savedAt
    )

    private fun mapToEntity(userId: Long, plate: SavedPlate): SavedPlateEntity = SavedPlateEntity(
        userId = userId,
        normalizedPlateCode = plate.normalizedPlateCode,
        formattedPlateCode = plate.formattedPlateCode,
        cityName = plate.cityName,
        ratingAverage = plate.ratingAverage,
        commentCount = plate.commentCount,
        reportTypes = plate.reportTypes.map(::mapReportTypeToLocal),
        savedAt = plate.savedAt
    )

    private fun mapReportTypeToLocal(type: ReportType): RecentSearchReportTypeLocal =
        RecentSearchReportTypeLocal(
            code = type.code,
            label = type.label,
            description = type.description,
            iconKey = type.iconKey,
            severity = type.severity,
            colorHex = type.colorHex,
            weight = type.weight,
            sortOrder = type.sortOrder
        )

    private fun mapReportTypeToDomain(type: RecentSearchReportTypeLocal): ReportType = ReportType(
        code = type.code,
        label = type.label,
        description = type.description,
        iconKey = type.iconKey,
        severity = type.severity,
        colorHex = type.colorHex,
        weight = type.weight,
        sortOrder = type.sortOrder
    )
}
