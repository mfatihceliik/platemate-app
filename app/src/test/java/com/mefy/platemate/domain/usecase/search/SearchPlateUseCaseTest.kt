package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.repository.PlateRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchPlateUseCaseTest {

    @Test
    fun invoke_normalizesInputBeforeDelegatingToRepository() = runTest {
        val repository = FakePlateRepository()
        val useCase = SearchPlateUseCase(repository)

        useCase(" 34 abc 123 ")

        assertEquals("34ABC123", repository.lastPlateCode)
        assertEquals(1, repository.callCount)
    }

    private class FakePlateRepository : PlateRepository {
        var lastPlateCode: String? = null
        var callCount: Int = 0

        override suspend fun searchPlate(plateCode: String): AppResult<PlateSearchResult> {
            callCount++
            lastPlateCode = plateCode
            return AppResult.Success(
                PlateSearchResult(
                    id = 1L,
                    plateCode = "34ABC123",
                    cityName = "Istanbul",
                    ratingAverage = 4.5,
                    reviewCount = 15L,
                    ratingDistribution = emptyList(),
                    tagSummary = emptyList(),
                    recentReviews = emptyList(),
                    totalSearchCount = 5L,
                    totalReviewCount = 2L,
                    totalReportCount = 1L,
                    score = 0.0,
                    lastActivityAt = "2026-05-19T00:00:00Z",
                    topReportTypes = listOf(
                        ReportType(
                            code = "SAFE",
                            label = "Safe",
                            description = "Safe driving",
                            iconKey = "shield",
                            severity = "LOW",
                            colorHex = "#00AA00",
                            weight = 1,
                            sortOrder = 1
                        )
                    )
                )
            )
        }
    }
}
