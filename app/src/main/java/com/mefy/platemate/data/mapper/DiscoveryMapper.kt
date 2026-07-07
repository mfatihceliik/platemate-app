package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.data.remote.dto.city.CityStatsDto
import com.mefy.platemate.data.remote.dto.DailyStatsDto
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import com.mefy.platemate.data.remote.dto.DiscoveryTabsDto
import com.mefy.platemate.data.remote.dto.RecentActivityDto
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto
import com.mefy.platemate.data.remote.dto.city.TopCityPlateDto
import com.mefy.platemate.domain.model.discovery.CityStats
import com.mefy.platemate.domain.model.discovery.DailyStats
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.discovery.RecentActivity
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.discovery.TopCityPlate
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.report.ReportType
import javax.inject.Inject

class DiscoveryMapper @Inject constructor() : Mapper<DiscoveryHomeResponseDto, DiscoveryHome> {

    override fun map(input: DiscoveryHomeResponseDto): DiscoveryHome {
        return DiscoveryHome(
            dailyStats = input.dailyStats?.let { mapDailyStats(it) } ?: DailyStats(0, 0, 0),
            tabs = input.tabs?.let { mapTabs(it) } ?: DiscoveryTabs(
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            ),
            cityStats = input.cityStats?.map { mapCityStats(it) } ?: emptyList(),
            topCityPlates = input.topCityPlates?.map { mapTopCityPlate(it) } ?: emptyList(),
            recentActivities = input.recentActivities?.map { mapRecentActivity(it) } ?: emptyList()
        )
    }

    private fun mapDailyStats(dto: DailyStatsDto): DailyStats {
        return DailyStats(
            todaySearchCount = dto.todaySearchCount,
            todayReviewCount = dto.todayReviewCount,
            todayReportCount = dto.todayReportCount
        )
    }

    private fun mapTabs(dto: DiscoveryTabsDto): DiscoveryTabs {
        return DiscoveryTabs(
            trendPlates = dto.trendPlates?.map { mapPlateDetail(it) } ?: emptyList(),
            attentionPlates = dto.dangerousPlates?.map { mapPlateDetail(it) } ?: emptyList(),
            goodDriverPlates = dto.goodDriverPlates?.map { mapPlateDetail(it) } ?: emptyList(),
            newPlates = dto.newPlates?.map { mapPlateDetail(it) } ?: emptyList()
        )
    }
    private fun mapPlateDetail(dto: PlateDetailDto): PlateDetail {
        return PlateDetail(
            plateCode = dto.plateCode.orEmpty(),
            cityName = dto.cityName,
            ratingAverage = dto.ratingAverage,
            reviewCount = dto.reviewCount,
            weeklySearchCount = dto.weeklySearchCount,
            todayReviewCount = dto.todayReviewCount,
            todayReportCount = dto.todayReportCount,
            todayWeightedReportScore = dto.todayWeightedReportScore,
            score = dto.score,
            lastActivityAt = dto.lastActivityAt.orEmpty(),
            topReportType = mapTopReports(dto.reportTypeDto)
        )
    }

    private fun mapTopReports(dto: List<ReportTypeDto>?): List<ReportType> {
        val mapped = dto?.map {
            mapTopReport(it)
        }
        return mapped ?: emptyList()
    }
    private fun mapTopReport(dto: ReportTypeDto): ReportType {
        return ReportType(
            code = dto.code,
            label = dto.label,
            description = dto.description,
            iconKey = dto.iconKey,
            severity = dto.severity,
            colorHex = dto.colorHex,
            weight = dto.weight,
            sortOrder = dto.sortOrder
        )
    }

    private fun mapCityStats(dto: CityStatsDto): CityStats {
        return CityStats(
            cityId = dto.cityId,
            cityName = dto.cityName.orEmpty(),
            todayReviewCount = dto.todayReviewCount
        )
    }

    private fun mapTopCityPlate(dto: TopCityPlateDto): TopCityPlate {
        return TopCityPlate(
            plateCode = dto.plateCode.orEmpty(),
            todayReviewCount = dto.todayReviewCount,
            todayReportCount = dto.todayReportCount,
            lastActivityAt = dto.lastActivityAt.orEmpty(),
            ratingAverage = dto.ratingAverage,
            reviewCount = dto.reviewCount
        )
    }

    private fun mapRecentActivity(dto: RecentActivityDto): RecentActivity {
        return RecentActivity(
            username = dto.username.orEmpty(),
            plateCode = dto.plateCode.orEmpty(),
            actionType = mapRecentActivityActionType(dto.actionType),
            occurredAt = dto.occurredAt.orEmpty(),
            rating = dto.rating,
            comment = dto.comment.orEmpty(),
            reportTypeCode = dto.reportTypeCode.orEmpty(),
            reportTypeLabel = dto.reportTypeLabel.orEmpty()
        )
    }

    private fun mapRecentActivityActionType(raw: String?): RecentActivityActionType {
        return when (raw) {
            "REVIEW_ADDED" -> RecentActivityActionType.REVIEW_ADDED
            "REPORT_ADDED" -> RecentActivityActionType.REPORT_ADDED
            "STARRED" -> RecentActivityActionType.STARRED
            "SAVED" -> RecentActivityActionType.SAVED
            "CLAIMED" -> RecentActivityActionType.CLAIMED
            else -> RecentActivityActionType.UNKNOWN
        }
    }
}
