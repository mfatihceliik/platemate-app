package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.R
import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.domain.model.discovery.DiscoveryFeedType
import com.mefy.platemate.domain.model.discovery.DiscoveryForYou
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.RecentActivity
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.common.formatter.RelativeTimeFormatter
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.DiscoverCityStatUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverForYouUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverMetricUiType
import com.mefy.platemate.presentation.features.uimodel.DiscoverRecentActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeCountUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase

@Singleton
class DefaultDiscoverUiMapper @Inject constructor(
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase
) :
    DiscoverUiMapper,
    Mapper<DiscoveryHome, DiscoverHomeUiData> {

    override fun map(input: DiscoveryHome): DiscoverHomeUiData = mapHome(input, emptySet())

    override fun mapHome(input: DiscoveryHome, bookmarkedCodes: Set<String>): DiscoverHomeUiData {
        val extendedStats = input.extendedStats
        val metrics = listOf(
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Search,
                valueText = input.dailyStats.todaySearchCount.toString(),
                labelResId = R.string.discover_metric_search_label,
                periodResId = R.string.discover_metric_today_period,
                deltaText = extendedStats?.searchDeltaPercent?.let(::formatDeltaText),
                deltaPositive = extendedStats?.searchDeltaPercent?.let { it >= 0.0 }
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Comment,
                valueText = input.dailyStats.todayReviewCount.toString(),
                labelResId = R.string.discover_metric_comment_label,
                periodResId = R.string.discover_metric_today_period,
                deltaText = extendedStats?.reviewDeltaPercent?.let(::formatDeltaText),
                deltaPositive = extendedStats?.reviewDeltaPercent?.let { it >= 0.0 }
            ),
            DiscoverMetricUiModel(
                type = DiscoverMetricUiType.Alert,
                valueText = input.dailyStats.todayReportCount.toString(),
                labelResId = R.string.discover_metric_active_alert_period,
                periodResId = R.string.discover_metric_today_period,
                deltaText = extendedStats?.reportDeltaPercent?.let(::formatDeltaText),
                // Rapor artisi olumsuz sinyaldir; dusus yesil gosterilir.
                deltaPositive = extendedStats?.reportDeltaPercent?.let { it <= 0.0 }
            )
        )

        // Sehir bazli bolum en fazla MAX_CITY_STATS sehir gosterir (backend de 5 dondurur; savunma amacli).
        val cappedCityStats = input.cityStats.take(MAX_CITY_STATS)
        val maxCityCount = cappedCityStats.maxOfOrNull { it.todayReviewCount } ?: 1L
        val cityStats = cappedCityStats.mapIndexed { index, stat ->
            DiscoverCityStatUiModel(
                rank = index + 1,
                cityId = stat.cityId,
                cityName = stat.cityName,
                count = stat.todayReviewCount.toInt(),
                progress = if (maxCityCount > 0L) {
                    stat.todayReviewCount.toFloat() / maxCityCount.toFloat()
                } else {
                    0f
                }
            )
        }

        val recentActivities = input.recentActivities.map { mapActivity(it) }

        val topReportTypes = extendedStats?.topReportTypesToday.orEmpty().map { reportType ->
            DiscoverReportTypeCountUiModel(
                code = reportType.code,
                label = reportType.label,
                colorHex = reportType.colorHex,
                count = reportType.count.toInt()
            )
        }

        return DiscoverHomeUiData(
            metrics = metrics,
            cityStats = cityStats,
            recentActivities = recentActivities,
            topReportTypes = topReportTypes,
            isPremium = input.feedType == DiscoveryFeedType.PREMIUM,
            forYou = input.forYou?.let { mapForYou(it, bookmarkedCodes) },
            tabs = input.tabs
        )
    }

    private fun mapForYou(forYou: DiscoveryForYou, bookmarkedCodes: Set<String>): DiscoverForYouUiModel {
        val weeklyStats = forYou.premiumStats?.let { stats ->
            listOf(
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Search,
                    valueText = stats.weeklySearchCount.toString(),
                    labelResId = R.string.discover_metric_search_label,
                    periodResId = R.string.discover_metric_week_period,
                    deltaText = formatDeltaText(stats.weeklySearchDeltaPercent),
                    deltaPositive = stats.weeklySearchDeltaPercent >= 0.0
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Comment,
                    valueText = stats.weeklyReviewCount.toString(),
                    labelResId = R.string.discover_metric_comment_label,
                    periodResId = R.string.discover_metric_week_period,
                    deltaText = formatDeltaText(stats.weeklyReviewDeltaPercent),
                    deltaPositive = stats.weeklyReviewDeltaPercent >= 0.0
                ),
                DiscoverMetricUiModel(
                    type = DiscoverMetricUiType.Alert,
                    valueText = stats.weeklyReportCount.toString(),
                    labelResId = R.string.discover_metric_active_alert_period,
                    periodResId = R.string.discover_metric_week_period,
                    deltaText = formatDeltaText(stats.weeklyReportDeltaPercent),
                    // Rapor artisi olumsuz sinyaldir; dusus yesil gosterilir.
                    deltaPositive = stats.weeklyReportDeltaPercent <= 0.0
                )
            )
        } ?: emptyList()

        return DiscoverForYouUiModel(
            followedPlates = mapForYouPlates(forYou.followedPlates, FOR_YOU_FOLLOWED_ID_SUFFIX, bookmarkedCodes),
            savedPlates = mapForYouPlates(forYou.savedPlates, FOR_YOU_SAVED_ID_SUFFIX, bookmarkedCodes),
            activities = forYou.followedPlateActivities.map { mapActivity(it) },
            weeklyStats = weeklyStats
        )
    }

    private fun mapForYouPlates(
        plates: List<PlateDetail>,
        idSuffix: String,
        bookmarkedCodes: Set<String>
    ): List<PlateDetailUiModel> = plates.mapIndexed { index, plate ->
        val normalizedCode = validateTurkishPlateUseCase.normalize(plate.plateCode)
        mapTabPlateToUiModel(
            plate = plate,
            rank = index + 1,
            idSuffix = idSuffix,
            isBookmarked = bookmarkedCodes.contains(normalizedCode)
        )
    }

    private fun mapActivity(activity: RecentActivity): DiscoverRecentActivityUiModel =
        DiscoverRecentActivityUiModel(
            id = "${activity.plateCode}_${activity.occurredAt}",
            type = activity.actionType,
            actorName = activity.username,
            actionText = UiText.Resource(activity.actionType.toActionTextRes()),
            plateCode = formatTurkishPlateInputUseCase(activity.plateCode),
            timeAgoText = RelativeTimeFormatter.format(activity.occurredAt)
        )

    private fun formatDeltaText(deltaPercent: Double): String {
        val rounded = deltaPercent.roundToInt()
        return if (rounded >= 0) "+$rounded%" else "$rounded%"
    }

    private fun RecentActivityActionType.toActionTextRes(): Int = when (this) {
        RecentActivityActionType.REVIEW_ADDED -> R.string.discover_activity_review_added
        RecentActivityActionType.RATING_GIVEN -> R.string.discover_activity_rating_given
        RecentActivityActionType.REPORT_SUBMITTED -> R.string.discover_activity_report_submitted
        RecentActivityActionType.UNKNOWN -> R.string.discover_activity_unknown
    }

    private companion object {
        // Feed id'leri "<plateCode>_<filterName>" oldugundan For You id'leri farkli suffix tasir;
        // ayni plaka feed'de ve For You'da ayni LazyColumn'da cakismadan listelenebilir.
        const val FOR_YOU_FOLLOWED_ID_SUFFIX = "ForYouFollowed"
        const val FOR_YOU_SAVED_ID_SUFFIX = "ForYouSaved"
        const val MAX_CITY_STATS = 5
    }

    override fun mapFeedPlates(
        plates: List<PlateDetail>,
        filter: String,
        bookmarkedCodes: Set<String>,
        startRank: Int
    ): List<PlateDetailUiModel> {
        return plates.mapIndexed { index, plate ->
            val normalizedCode = validateTurkishPlateUseCase.normalize(plate.plateCode)
            val isBookmarked = bookmarkedCodes.contains(normalizedCode)
            mapTabPlateToUiModel(plate = plate, rank = startRank + index, idSuffix = filter, isBookmarked = isBookmarked)
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
        idSuffix: String,
        isBookmarked: Boolean
    ): PlateDetailUiModel {
        val formattedPlateCode = formatTurkishPlateInputUseCase(plate.plateCode)
        return PlateDetailUiModel(
            id = "${plate.plateCode}_$idSuffix",
            rank = rank,
            plateCode = formattedPlateCode,
            reportTags = mapReportTags(plate.topReportType),
            cityName = CityNameResolver.resolveCityName(cityName = plate.cityName, plateCode = plate.plateCode),
            ratingAverage = plate.ratingAverage,
            commentCount = plate.reviewCount,
            searchCount = plate.weeklySearchCount,
            isBookmarked = isBookmarked,
            cityCode = formattedPlateCode.take(2),
            ratingText = NumberFormatter.formatRating(plate.ratingAverage)
        )
    }
}
