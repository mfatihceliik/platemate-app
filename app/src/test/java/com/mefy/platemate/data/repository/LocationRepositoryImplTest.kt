package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.UserLocationMapper
import com.mefy.platemate.data.remote.dto.user.UserLocationDto
import com.mefy.platemate.data.remote.rest.service.LocationApiService
import com.mefy.platemate.data.remote.websocket.datasource.SocketLocationDataSource
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.location.UserLocation
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocationRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun observeLocationUpdates_forwardsSocketValues() = runTest {
        val locations = MutableSharedFlow<UserLocation>()
        val repository = LocationRepositoryImpl(
            api = FakeLocationApiService(),
            socketLocationDataSource = object : SocketLocationDataSource {
                override fun observeLocations(): Flow<UserLocation> = locations
            },
            userLocationMapper = UserLocationMapper(),
            appDispatchers = testDispatchers()
        )

        val firstEmission = async { repository.observeLocationUpdates().first() }
        runCurrent()

        val expected = UserLocation(
            id = 1L,
            userId = 42L,
            username = "fatih",
            latitude = 41.0082,
            longitude = 28.9784,
            lastUpdatedAt = AppDateTime("2026-05-13T10:15:30Z")
        )

        locations.emit(expected)
        runCurrent()

        assertEquals(expected, firstEmission.await())
    }

    private fun testDispatchers(): AppDispatchers = AppDispatchers(
        main = mainDispatcherRule.dispatcher,
        io = mainDispatcherRule.dispatcher,
        default = mainDispatcherRule.dispatcher
    )

    private class FakeLocationApiService : LocationApiService {
        override suspend fun getUserLocation(userId: Long): DataResultResponse<UserLocationDto> =
            DataResultResponse(success = true, message = null, data = null)

        override suspend fun getVisibleLocations(): DataResultResponse<List<UserLocationDto>> =
            DataResultResponse(success = true, message = null, data = emptyList())

        override suspend fun blockUserFromLocation(targetUserId: Long): ResultResponse =
            ResultResponse(success = true, message = null)

        override suspend fun unblockUserFromLocation(targetUserId: Long): ResultResponse =
            ResultResponse(success = true, message = null)

        override suspend fun getBlockedLocationUsers(): DataResultResponse<List<Long>> =
            DataResultResponse(success = true, message = null, data = emptyList())
    }
}
