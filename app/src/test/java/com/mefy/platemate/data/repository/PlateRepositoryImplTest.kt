package com.mefy.platemate.data.repository

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateSearchResultMapper
import com.mefy.platemate.data.remote.dto.plate.PlateDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchRequest
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.data.remote.dto.review.ReviewDto
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
                    data = PlateSearchRequest(
                        id = 12L,
                        plateCode = "34ABC123",
                        cityName = "Istanbul",
                        ratingAverage = 4.2,
                        totalRatingSum = 30L,
                        totalSearchCount = 12L,
                        totalReviewCount = 7L,
                        totalReportCount = 1L,
                        totalWeightedReportScore = 2L,
                        score = 80,
                        lastActivityAt = "2026-05-19T00:00:00Z",
                        recentReviews = listOf(sampleReviewDto(plateCode = "34ABC123")),
                        recentReportTypes = listOf(sampleReportTypeDto())
                    )
                )
            )
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Success)
        val data = (result as AppResult.Success).data
        assertEquals(12L, data.id)
        assertEquals("34ABC123", data.plateCode)
        assertEquals("Istanbul", data.cityName)
        assertEquals(4.2, data.ratingAverage, 0.0)
        assertEquals(7L, data.totalReviewCount)
        assertEquals(30L, data.totalRatingSum)
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
        assertTrue((result as AppResult.Error).error is AppError.Backend)
    }

    @Test
    fun searchPlate_httpFailure_returnsHttpError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
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
        assertTrue((result as AppResult.Error).error is AppError.Http)
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
    fun searchPlate_serverUnavailableFailure_returnsServerUnavailableError() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val repository = createRepository(
            api = FakePlateApiService(throwable = ConnectException("connection refused"))
        )

        val result = repository.searchPlate("34ABC123")

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.ServerUnavailable)
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
        private val response: DataResultResponse<PlateSearchRequest>? = null,
        private val throwable: Throwable? = null
    ) : PlateApiService {
        override suspend fun searchPlate(plate: String): DataResultResponse<PlateSearchRequest> {
            throwable?.let { throw it }
            return response ?: error("Response must be provided for this test case.")
        }

        override suspend fun searchPlateByPath(plate: String): DataResultResponse<PlateDto> {
            throw UnsupportedOperationException("Not used in this test")
        }
    }

    private companion object {
        fun sampleReviewDto(plateCode: String): ReviewDto = ReviewDto(
            id = 1L,
            plateCode = plateCode,
            rating = 4,
            comment = "safe",
            userId = 2L,
            reviewerUsername = "user",
            createdAt = null,
            updatedAt = null
        )

        fun sampleReportTypeDto(): ReportTypeDto = ReportTypeDto(
            code = "SAFE",
            label = "Safe",
            description = "Safe driving",
            iconKey = "shield",
            severity = "LOW",
            colorHex = "#00AA00",
            weight = 1,
            sortOrder = 1
        )
    }
}

