package com.mefy.platemate.data.repository

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateSearchResultMapper
import com.mefy.platemate.data.remote.dto.plate.PlateDetailReviewItemDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchResponseDto
import com.mefy.platemate.data.remote.dto.plate.PlateTagSummaryItemDto
import com.mefy.platemate.data.remote.dto.plate.RatingDistributionItemDto
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.testutil.MainDispatcherRule
import java.io.IOException
import java.net.ConnectException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PlateRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun searchPlate_success_mapsDtoToDomainModel() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(
                response = DataResultResponse(
                    success = true,
                    message = null,
                    data = PlateSearchResponseDto(
                        id = 1L,
                        plateCode = "34ABC123",
                        cityName = "Istanbul",
                        ratingAverage = 4.2,
                        reviewCount = 7,
                        totalRatingSum = 30L,
                        ratingDistribution = listOf(
                            RatingDistributionItemDto(rating = 5, count = 4, percentage = 57.14)
                        ),
                        tagSummary = listOf(
                            PlateTagSummaryItemDto(code = "SAFE", label = "Safe", iconKey = "shield", colorHex = "#00AA00", count = 3)
                        ),
                        totalSearchCount = 12L,
                        totalReviewCount = 7L,
                        totalReportCount = 1L,
                        totalWeightedReportScore = 2L,
                        score = 80.0,
                        lastActivityAt = "2026-05-19T00:00:00Z",
                        recentReviews = listOf(
                            PlateDetailReviewItemDto(id = 1, userId = 1, username = "mfy", displayName = "Fatih", profilePhotoUrl = null, rating = 5, comment = "Great", reportTags = listOf("SAFE"), createdAt = "2026-05-19T00:00:00Z")
                        )
                    )
                )
            )
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Success)
        val data = (result as AppResult.Success).data
        assertEquals("34ABC123", data.plateCode)
        assertEquals("Istanbul", data.cityName)
        assertEquals(4.2, data.ratingAverage, 0.0)
        assertEquals(7L, data.reviewCount)
        assertEquals(1, data.ratingDistribution.size)
        assertEquals(1, data.tagSummary.size)
        assertEquals(1, data.recentReviews.size)
        assertEquals(1, data.topReportTypes.size)
    }

    @Test
    fun searchPlate_backendFailure_returnsBackendError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(
                response = DataResultResponse(
                    success = false,
                    message = "not found",
                    data = null
                )
            )
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Server)
    }

    @Test
    fun searchPlate_http4xxFailure_returnsServerError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(
                throwable = HttpException(
                    Response.error<String>(
                        404,
                        """{"success":false,"message":"not found","data":null}"""
                            .toResponseBody("application/json".toMediaType())
                    )
                )
            )
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Server)
    }

    @Test
    fun searchPlate_networkFailure_returnsNetworkError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(throwable = IOException("offline"))
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun searchPlate_connectFailure_returnsUnreachableError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(throwable = ConnectException("connection refused"))
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Unreachable)
    }

    private fun createRepository(api: FakePlateApiService): PlateRepositoryImpl = PlateRepositoryImpl(
        api = api,
        mapper = PlateSearchResultMapper(),
        appDispatchers = AppDispatchers(
            main = mainDispatcherRule.dispatcher,
            io = mainDispatcherRule.dispatcher,
            default = mainDispatcherRule.dispatcher
        )
    )

    private class FakePlateApiService(
        private val response: DataResultResponse<PlateSearchResponseDto>? = null,
        private val throwable: Throwable? = null
    ) : PlateApiService {
        override suspend fun searchPlate(plate: String): DataResultResponse<PlateSearchResponseDto> {
            throwable?.let { throw it }
            return response ?: error("Response must be provided for this test case.")
        }
    }
}
