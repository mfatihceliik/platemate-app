package com.mefy.platemate.presentation.features.main.search.mapper

import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultSearchUiMapperTest {

    private val mapper = DefaultSearchUiMapper()

    @Test
    fun detectCityFromPlate_extractsFromLeadingDigits() {
        val result = mapper.detectCityFromPlate("43ABC123")

        assertEquals("K\u00fctahya", result)
    }

    @Test
    fun resolveCityName_repairsMojibakeValue() {
        val result = mapper.resolveCityName("K\u00c3\u00bctahya", "43ABC123")

        assertEquals("K\u00fctahya", result)
    }

    @Test
    fun mapRecentSearch_mapsExpectedDomainFields() {
        val item = mapper.mapRecentSearch(
            result = sampleResult(),
            normalizedPlate = "34ABC123",
            formattedPlate = "34 ABC 123",
            detectedCityName = "Istanbul"
        )

        assertEquals("34ABC123", item.normalizedPlateCode)
        assertEquals("34 ABC 123", item.formattedPlateCode)
        assertEquals("Istanbul", item.cityName)
        assertEquals(2, item.reportTypes.size)
        assertEquals("SAFE", item.reportTypes.first().code)
        assertEquals("SPEEDING", item.reportTypes[1].code)
        assertEquals("#00AA00", item.reportTypes.first().colorHex)
        assertEquals(4.3, item.ratingAverage, 0.0)
        assertEquals(12L, item.commentCount)
    }

    @Test
    fun mapRecentSearchItem_mapsExpectedUiModelFields() {
        val recent = mapper.mapRecentSearch(
            result = sampleResult(),
            normalizedPlate = "34ABC123",
            formattedPlate = "34 ABC 123",
            detectedCityName = "Istanbul"
        )
        val item = mapper.mapRecentSearchItem(recent, isBookmarked = true)

        assertEquals("34ABC123", item.normalizedPlateCode)
        assertEquals("34 ABC 123", item.plateCode)
        assertEquals("Istanbul", item.cityName)
        assertEquals(2, item.reportTags.size)
        assertEquals("SAFE", item.reportTags.first().code)
        assertEquals("SPEEDING", item.reportTags[1].code)
        assertEquals("#00AA00", item.reportTags.first().colorHex)
        assertEquals(4.3, item.ratingAverage, 0.0)
        assertEquals(12L, item.commentCount)
        assertEquals(true, item.isBookmarked)
    }

    @Test
    fun mapRecentSearches_mergesBookmarkCodes() {
        val recent = mapper.mapRecentSearch(
            result = sampleResult(),
            normalizedPlate = "34ABC123",
            formattedPlate = "34 ABC 123",
            detectedCityName = "Istanbul"
        )

        val items = mapper.mapRecentSearches(
            items = listOf(recent),
            bookmarkedCodes = setOf("34ABC123")
        )

        assertEquals(1, items.size)
        assertEquals(true, items.first().isBookmarked)
    }

    @Test
    fun mapSavedPlate_mapsAllSnapshotFields() {
        val recent = mapper.mapRecentSearch(
            result = sampleResult(),
            normalizedPlate = "34ABC123",
            formattedPlate = "34 ABC 123",
            detectedCityName = "Istanbul"
        )

        val saved = mapper.mapSavedPlate(recent)

        assertEquals("34ABC123", saved.normalizedPlateCode)
        assertEquals("34 ABC 123", saved.formattedPlateCode)
        assertEquals("Istanbul", saved.cityName)
        assertEquals(4.3, saved.ratingAverage, 0.0)
        assertEquals(12L, saved.commentCount)
        assertEquals(2, saved.reportTypes.size)
        assertEquals("SAFE", saved.reportTypes.first().code)
    }

    private fun sampleResult(): PlateSearchResult = PlateSearchResult(
        id = 10L,
        plateCode = "34ABC123",
        cityName = "Istanbul",
        ratingAverage = 4.3,
        totalRatingSum = 52L,
        totalSearchCount = 10L,
        totalReviewCount = 12L,
        totalReportCount = 0L,
        totalWeightedReportScore = 0L,
        score = 0,
        lastActivityAt = "2026-05-19T00:00:00Z",
        recentReviews = listOf(
            Review(
                id = 1L,
                plateCode = "34ABC123",
                rating = 5,
                comment = "great",
                userId = 2L,
                reviewerUsername = "user",
                createdAt = null,
                updatedAt = null
            )
        ),
        recentReportTypes = listOf(
            ReportType(
                code = "SAFE",
                label = "Safe",
                description = "Safe driving",
                iconKey = "shield",
                severity = "LOW",
                colorHex = "#00AA00",
                weight = 1,
                sortOrder = 1
            ),
            ReportType(
                code = "SPEEDING",
                label = "Speeding",
                description = "Speeding",
                iconKey = "speed",
                severity = "HIGH",
                colorHex = "#FF0000",
                weight = 8,
                sortOrder = 2
            )
        )
    )
}
