package com.mefy.platemate.presentation.features.main.discover.mapper

import com.mefy.platemate.R
import com.mefy.platemate.domain.model.discovery.CityStats
import com.mefy.platemate.domain.model.discovery.DailyStats
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.discovery.RecentActivity
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.discovery.TopCityPlate
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.presentation.features.main.discover.DiscoverFilterUi
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultDiscoverUiMapperTest {

    private val mapper = DefaultDiscoverUiMapper()

    @Test
    fun mapHome_mapsMetricsCityProgressAndRecentActivities() {
        val mapped = mapper.mapHome(sampleDiscoveryHome())

        assertEquals(3, mapped.metrics.size)
        assertEquals("12", mapped.metrics[0].valueText)
        assertEquals(R.string.discover_metric_search_label, mapped.metrics[0].labelResId)
        assertEquals(2, mapped.cityStats.size)
        assertEquals(1f, mapped.cityStats[0].progress, 0.0001f)
        assertEquals(0.4f, mapped.cityStats[1].progress, 0.0001f)
        assertEquals(2, mapped.recentActivities.size)
        assertEquals("34ABC123_2026-05-18T10:00:00", mapped.recentActivities[0].id)
    }

    @Test
    fun mapTabPlates_appliesFilterAndRanksFromOne() {
        val tabs = sampleDiscoveryHome().tabs

        val attention = mapper.mapTabPlates(tabs, DiscoverFilterUi.Attention)
        val trend = mapper.mapTabPlates(tabs, DiscoverFilterUi.Trend)

        assertEquals(1, attention.size)
        assertEquals(1, attention.first().rank)
        assertEquals("35DNG111", attention.first().plateCode)
        assertEquals("Istanbul", attention.first().cityName)
        assertEquals(4.4, attention.first().ratingAverage, 0.0)
        assertEquals(1, attention.first().reportTags.size)
        assertEquals("#FF0000", attention.first().reportTags.first().colorHex)
        assertEquals(2, trend.size)
        assertEquals(1, trend[0].rank)
        assertEquals(2, trend[1].rank)
        assertEquals(4.4, trend[0].ratingAverage, 0.0)
    }

    private fun sampleDiscoveryHome(): DiscoveryHome = DiscoveryHome(
        dailyStats = DailyStats(
            todaySearchCount = 12L,
            todayReviewCount = 8L,
            todayReportCount = 3L
        ),
        tabs = DiscoveryTabs(
            trendPlates = listOf(
                samplePlate("34ABC123", score = 9.2),
                samplePlate("06XYZ987", score = 8.4)
            ),
            attentionPlates = listOf(samplePlate("35DNG111", score = 7.1)),
            goodDriverPlates = listOf(samplePlate("16GOOD16", score = 9.8)),
            newPlates = listOf(samplePlate("34NEW001", score = 6.9))
        ),
        cityStats = listOf(
            CityStats(cityId = 34, cityName = "Istanbul", todayReviewCount = 10L),
            CityStats(cityId = 6, cityName = "Ankara", todayReviewCount = 4L)
        ),
        topCityPlates = listOf(
            TopCityPlate(
                plateCode = "34ABC123",
                todayReviewCount = 8L,
                todayReportCount = 1L,
                lastActivityAt = "2026-05-18T08:30:00",
                ratingAverage = 4.8,
                reviewCount = 18L
            )
        ),
        recentActivities = listOf(
            RecentActivity(
                username = "fatih",
                plateCode = "34ABC123",
                actionType = RecentActivityActionType.REVIEW_ADDED,
                occurredAt = "2026-05-18T10:00:00",
                rating = 4.0,
                comment = "iyi",
                reportTypeCode = "",
                reportTypeLabel = ""
            ),
            RecentActivity(
                username = "ali",
                plateCode = "06XYZ987",
                actionType = RecentActivityActionType.REPORT_ADDED,
                occurredAt = "2026-05-18T09:00:00",
                rating = 0.0,
                comment = "",
                reportTypeCode = "DANGER",
                reportTypeLabel = "Danger"
            )
        )
    )

    private fun samplePlate(plateCode: String, score: Double): PlateDetail = PlateDetail(
        plateCode = plateCode,
        cityName = "Istanbul",
        ratingAverage = 4.4,
        reviewCount = 5L,
        todaySearchCount = 7L,
        todayReviewCount = 3L,
        todayReportCount = 1L,
        todayWeightedReportScore = 1.5,
        score = score,
        lastActivityAt = "2026-05-18T10:00:00",
        topReportType = listOf(
            ReportType(
                code = "DANGER",
                label = "Danger",
                description = "Danger behavior",
                iconKey = "alert",
                severity = "HIGH",
                colorHex = "#FF0000",
                weight = 10,
                sortOrder = 1
            )
        )
    )
}
