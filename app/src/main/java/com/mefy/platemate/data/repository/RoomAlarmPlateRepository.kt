package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.room.dao.AlarmPlateDao
import com.mefy.platemate.data.local.room.entity.AlarmPlateEntity
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.repository.AlarmPlateRepository
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
class RoomAlarmPlateRepository @Inject constructor(
    private val alarmPlateDao: AlarmPlateDao,
    private val plateApiService: PlateApiService,
    private val sessionStore: SessionStore
) : AlarmPlateRepository {

    override fun observeAlarmPlates(): Flow<List<AlarmPlate>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            alarmPlateDao.observeAlarmPlates(userId = userId)
                .map { entities -> entities.map(::mapToDomain) }
        }

    override fun observeAlarmPlateCodes(): Flow<Set<String>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptySet())
            alarmPlateDao.observeAlarmPlateCodes(userId = userId)
                .map { codes -> codes.toSet() }
        }

    override suspend fun toggleAlarm(plate: AlarmPlate): AppResult<Boolean> {
        val userId = sessionStore.session.first()?.userId ?: return AppResult.Success(false)
        val deletedCount = alarmPlateDao.deleteByNormalizedPlate(
            userId = userId,
            normalizedPlateCode = plate.normalizedPlateCode
        )

        return if (deletedCount > 0) {
            // Was alarmed → remove on backend; restore local on failure.
            when (val remote = safeResultCall { plateApiService.removeAlarm(plate.normalizedPlateCode) }) {
                is AppResult.Success -> AppResult.Success(false)
                is AppResult.Error -> {
                    alarmPlateDao.upsert(mapToEntity(userId, plate))
                    AppResult.Error(remote.error)
                }
            }
        } else {
            // Was not alarmed → create on backend first (limit may reject), then cache.
            when (val remote = safeResultCall { plateApiService.createAlarm(plate.normalizedPlateCode) }) {
                is AppResult.Success -> {
                    alarmPlateDao.upsert(mapToEntity(userId, plate))
                    AppResult.Success(true)
                }
                is AppResult.Error -> AppResult.Error(remote.error)
            }
        }
    }

    override suspend fun replaceFromRemote(plates: List<AlarmPlate>) {
        val userId = sessionStore.session.first()?.userId ?: return
        alarmPlateDao.deleteAllForUser(userId)
        plates.forEach { plate -> alarmPlateDao.upsert(mapToEntity(userId = userId, plate = plate)) }
    }

    private fun mapToDomain(entity: AlarmPlateEntity): AlarmPlate = AlarmPlate(
        normalizedPlateCode = entity.normalizedPlateCode,
        formattedPlateCode = entity.formattedPlateCode,
        cityName = entity.cityName,
        ratingAverage = entity.ratingAverage,
        commentCount = entity.commentCount,
        reportTypes = entity.reportTypes.map(::mapReportTypeToDomain),
        savedAt = entity.savedAt
    )

    private fun mapToEntity(userId: Long, plate: AlarmPlate): AlarmPlateEntity = AlarmPlateEntity(
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
