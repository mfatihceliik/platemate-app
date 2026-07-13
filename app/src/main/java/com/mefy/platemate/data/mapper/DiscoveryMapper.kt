package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.data.remote.dto.city.CityPlateActivityDto
import com.mefy.platemate.data.remote.dto.city.CityStatsDto
import com.mefy.platemate.data.remote.dto.DailyStatsDto
import com.mefy.platemate.data.remote.dto.DiscoveryExtendedStatsDto
import com.mefy.platemate.data.remote.dto.DiscoveryForYouDto
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import com.mefy.platemate.data.remote.dto.DiscoveryPremiumStatsDto
import com.mefy.platemate.data.remote.dto.DiscoveryReportTypeCountDto
import com.mefy.platemate.data.remote.dto.DiscoveryTabsDto
import com.mefy.platemate.data.remote.dto.RecentActivityDto
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto
import com.mefy.platemate.data.remote.dto.city.TopCityPlateDto
import com.mefy.platemate.domain.model.discovery.CityPlateActivity
import com.mefy.platemate.domain.model.discovery.CityPlatePage
import com.mefy.platemate.domain.model.discovery.CityStats
import com.mefy.platemate.domain.model.discovery.DailyStats
import com.mefy.platemate.domain.model.discovery.DiscoveryExtendedStats
import com.mefy.platemate.domain.model.discovery.DiscoveryFeedType
import com.mefy.platemate.domain.model.discovery.DiscoveryForYou
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryPremiumStats
import com.mefy.platemate.domain.model.discovery.DiscoveryTabPage
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.discovery.RecentActivity
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.discovery.ReportTypeCount
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
            recentActivities = input.recentActivities?.map { mapRecentActivity(it) } ?: emptyList(),
            feedType = DiscoveryFeedType.fromString(input.feedType),
            extendedStats = input.extendedStats?.let { mapExtendedStats(it) },
            forYou = input.forYou?.let { mapForYou(it) }
        )
    }

    private fun mapForYou(dto: DiscoveryForYouDto): DiscoveryForYou {
        return DiscoveryForYou(
            followedPlates = dto.followedPlates?.map { mapPlateDetail(it) } ?: emptyList(),
            savedPlates = dto.savedPlates?.map { mapPlateDetail(it) } ?: emptyList(),
            followedPlateActivities = dto.followedPlateActivities?.map { mapRecentActivity(it) } ?: emptyList(),
            premiumStats = dto.premiumStats?.let { mapPremiumStats(it) }
        )
    }

    private fun mapPremiumStats(dto: DiscoveryPremiumStatsDto): DiscoveryPremiumStats {
        return DiscoveryPremiumStats(
            weeklySearchCount = dto.weeklySearchCount ?: 0L,
            weeklyReviewCount = dto.weeklyReviewCount ?: 0L,
            weeklyReportCount = dto.weeklyReportCount ?: 0L,
            weeklySearchDeltaPercent = dto.weeklySearchDeltaPercent ?: 0.0,
            weeklyReviewDeltaPercent = dto.weeklyReviewDeltaPercent ?: 0.0,
            weeklyReportDeltaPercent = dto.weeklyReportDeltaPercent ?: 0.0
        )
    }

    private fun mapExtendedStats(dto: DiscoveryExtendedStatsDto): DiscoveryExtendedStats {
        return DiscoveryExtendedStats(
            yesterdaySearchCount = dto.yesterdaySearchCount ?: 0L,
            yesterdayReviewCount = dto.yesterdayReviewCount ?: 0L,
            yesterdayReportCount = dto.yesterdayReportCount ?: 0L,
            searchDeltaPercent = dto.searchDeltaPercent ?: 0.0,
            reviewDeltaPercent = dto.reviewDeltaPercent ?: 0.0,
            reportDeltaPercent = dto.reportDeltaPercent ?: 0.0,
            topReportTypesToday = dto.topReportTypesToday?.map { mapReportTypeCount(it) } ?: emptyList()
        )
    }

    private fun mapReportTypeCount(dto: DiscoveryReportTypeCountDto): ReportTypeCount {
        return ReportTypeCount(
            code = dto.code.orEmpty(),
            label = dto.label.orEmpty(),
            colorHex = dto.colorHex.orEmpty(),
            iconKey = dto.iconKey.orEmpty(),
            count = dto.count ?: 0L
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
            attentionPlates = dto.attentionPlates?.map { mapPlateDetail(it) } ?: emptyList(),
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
            code = dto.code.orEmpty(),
            label = dto.label.orEmpty(),
            description = dto.description.orEmpty(),
            iconKey = dto.iconKey.orEmpty(),
            severity = dto.severity.orEmpty(),
            colorHex = dto.colorHex.orEmpty(),
            weight = dto.weight ?: 0,
            sortOrder = dto.sortOrder ?: 0
        )
    }

    fun mapTabPage(dto: PagedResult<PlateDetailDto>): DiscoveryTabPage {
        return DiscoveryTabPage(
            items = dto.items.map { mapPlateDetail(it) },
            page = dto.meta.page,
            hasNext = dto.meta.hasNext
        )
    }

    fun mapCityPlatePage(dto: PagedResult<CityPlateActivityDto>): CityPlatePage {
        return CityPlatePage(
            items = dto.items.map { mapCityPlateActivity(it) },
            page = dto.meta.page,
            hasNext = dto.meta.hasNext
        )
    }

    private fun mapCityPlateActivity(dto: CityPlateActivityDto): CityPlateActivity {
        return CityPlateActivity(
            plateCode = dto.plateCode.orEmpty(),
            todayReviewCount = dto.todayReviewCount ?: 0L,
            todayReportCount = dto.todayReportCount ?: 0L,
            lastActivityAt = dto.lastActivityAt.orEmpty(),
            ratingAverage = dto.ratingAverage ?: 0.0,
            reviewCount = dto.reviewCount ?: 0L
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
            "RATING_GIVEN" -> RecentActivityActionType.RATING_GIVEN
            "REPORT_SUBMITTED" -> RecentActivityActionType.REPORT_SUBMITTED
            else -> RecentActivityActionType.UNKNOWN
        }
    }
}
