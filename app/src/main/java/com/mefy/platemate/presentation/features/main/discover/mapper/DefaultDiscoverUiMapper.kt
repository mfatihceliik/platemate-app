package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.R
import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.features.main.discover.DiscoverFilterUi
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.main.discover.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import javax.inject.Inject
import javax.inject.Singleton

import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase

@Singleton
class DefaultDiscoverUiMapper @Inject constructor(
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase
) :
    DiscoverUiMapper,
    Mapper<DiscoveryHome, DiscoverHomeUiData> {

    override fun map(input: DiscoveryHome): DiscoverHomeUiData = mapHome(input)

    override fun mapHome(input: DiscoveryHome): DiscoverHomeUiData {
        val metrics = listOf(
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Search,
                valueText = input.dailyStats.todaySearchCount.toString(),
                labelResId = R.string.discover_metric_search_label,
                periodResId = R.string.discover_metric_today_period
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Comment,
                valueText = input.dailyStats.todayReviewCount.toString(),
                labelResId = R.string.discover_metric_comment_label,
                periodResId = R.string.discover_metric_today_period
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Alert,
                valueText = input.dailyStats.todayReportCount.toString(),
                labelResId = R.string.discover_metric_active_alert_period,
                periodResId = R.string.discover_metric_today_period
            )
        )

        val maxCityCount = input.cityStats.maxOfOrNull { it.todayReviewCount } ?: 1L
        val cityStats = input.cityStats.mapIndexed { index, stat ->
            DiscoverCityStatUiModel(
                rank = index + 1,
                cityName = stat.cityName,
                count = stat.todayReviewCount.toInt(),
                progress = if (maxCityCount > 0L) {
                    stat.todayReviewCount.toFloat() / maxCityCount.toFloat()
                } else {
                    0f
                }
            )
        }

        val recentActivities = input.recentActivities.map { activity ->
            DiscoverRecentActivityUiModel(
                id = "${activity.plateCode}_${activity.occurredAt}",
                type = activity.actionType,
                actorName = activity.username,
                actionText = activity.actionType.name,
                plateCode = activity.plateCode,
                timeAgoText = activity.occurredAt
            )
        }

        return DiscoverHomeUiData(
            metrics = metrics,
            cityStats = cityStats,
            recentActivities = recentActivities,
            tabs = input.tabs
        )
    }

    override fun mapTabPlates(
        tabs: DiscoveryTabs,
        filter: DiscoverFilterUi,
        bookmarkedCodes: Set<String>
    ): List<PlateDetailUiModel> {
        val plates = when (filter) {
            DiscoverFilterUi.Trend -> tabs.trendPlates
            DiscoverFilterUi.Careless -> tabs.attentionPlates
            DiscoverFilterUi.GoodDriver -> tabs.goodDriverPlates
            DiscoverFilterUi.Newest -> tabs.newPlates
        }

        return plates.mapIndexed { index, plate ->
            val normalizedCode = validateTurkishPlateUseCase.normalize(plate.plateCode)
            val isBookmarked = bookmarkedCodes.contains(normalizedCode)
            mapTabPlateToUiModel(plate = plate, rank = index + 1, filter = filter, isBookmarked = isBookmarked)
        }
    }

    private fun mapReportTags(reportTypes: List<ReportType>): List<PlateReportTagUiModel> =
        reportTypes.map { reportType ->
            PlateReportTagUiModel(
                code = reportType.code,
                label = reportType.label,
                severity = reportType.severity,
                colorHex = reportType.colorHex
            )
        }

    private fun mapTabPlateToUiModel(
        plate: PlateDetail,
        rank: Int,
        filter: DiscoverFilterUi,
        isBookmarked: Boolean
    ): PlateDetailUiModel {
        val formattedPlateCode = formatTurkishPlateInputUseCase(plate.plateCode)
        return PlateDetailUiModel(
            id = "${plate.plateCode}_${filter.name}",
            rank = rank,
            plateCode = formattedPlateCode,
            reportTags = mapReportTags(plate.topReportType),
            cityName = CityNameResolver.resolveCityName(cityName = plate.cityName, plateCode = plate.plateCode),
            ratingAverage = plate.ratingAverage,
            commentCount = plate.reviewCount,
            searchCount = plate.weeklySearchCount,
            isBookmarked = isBookmarked,
            cityCode = formattedPlateCode.take(2),
            ratingText = String.format("%.1f", plate.ratingAverage)
        )
    }
}
