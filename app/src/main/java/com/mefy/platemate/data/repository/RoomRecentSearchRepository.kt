package com.mefy.platemate.data.repository

import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.local.room.dao.RecentSearchDao
import com.mefy.platemate.data.local.room.entity.RecentSearchEntity
import com.mefy.platemate.data.local.room.model.RecentSearchReportTypeLocal
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.repository.RecentSearchRepository
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
class RoomRecentSearchRepository @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
    private val sessionStore: SessionStore
) : RecentSearchRepository {

    override fun observeRecent(): Flow<List<RecentSearch>> =
        sessionStore.session.flatMapLatest { session ->
            val userId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            recentSearchDao.observeRecent(userId = userId, limit = MAX_RECENT_ITEMS)
                .map { entities -> entities.map(::mapToDomain) }
        }

    override suspend fun upsertRecent(item: RecentSearch) {
        val userId = sessionStore.session.first()?.userId ?: return
        recentSearchDao.deleteRecent(userId = userId, normalizedPlateCode = item.normalizedPlateCode)
        recentSearchDao.upsert(mapToEntity(userId = userId, item = item))
        recentSearchDao.trimToLimit(userId = userId, limit = MAX_RECENT_ITEMS)
    }

    override suspend fun deleteRecent(normalizedPlateCode: String) {
        val userId = sessionStore.session.first()?.userId ?: return
        recentSearchDao.deleteRecent(userId = userId, normalizedPlateCode = normalizedPlateCode)
    }

    override suspend fun clearRecent() {
        val userId = sessionStore.session.first()?.userId ?: return
        recentSearchDao.clearRecent(userId = userId)
    }

    private fun mapToDomain(entity: RecentSearchEntity): RecentSearch = RecentSearch(
        normalizedPlateCode = entity.normalizedPlateCode,
        formattedPlateCode = entity.formattedPlateCode,
        cityName = entity.cityName,
        ratingAverage = entity.ratingAverage,
        commentCount = entity.commentCount,
        reportTypes = entity.reportTypes.map(::mapReportTypeToDomain)
    )

    private fun mapToEntity(userId: Long, item: RecentSearch): RecentSearchEntity = RecentSearchEntity(
        userId = userId,
        normalizedPlateCode = item.normalizedPlateCode,
        formattedPlateCode = item.formattedPlateCode,
        cityName = item.cityName,
        ratingAverage = item.ratingAverage,
        commentCount = item.commentCount,
        reportTypes = item.reportTypes.map(::mapReportTypeToLocal),
        searchedAt = System.currentTimeMillis()
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

    private companion object {
        const val MAX_RECENT_ITEMS = 6
    }
}
