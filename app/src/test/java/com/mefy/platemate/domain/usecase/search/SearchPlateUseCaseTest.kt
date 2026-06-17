package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
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
                    ratingAverage = 4.5,
                    reviewCount = 15,
                    todaySearchCount = 5,
                    todayReviewCount = 2,
                    todayReportCount = 1,
                    todayWeightedReportScore = 3.0,
                    topReportTypes = emptyList(),
                    recentReviews = listOf(
                        Review(
                            id = 1L,
                            plateCode = plateCode,
                            rating = 0,
                            comment = "",
                            reviewStatus = "APPROVED",
                            userId = 1L,
                            reviewerUsername = "user",
                            createdAt = null,
                            updatedAt = null
                        )
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
                        )
                    )
                )
            )
        }
    }
}
