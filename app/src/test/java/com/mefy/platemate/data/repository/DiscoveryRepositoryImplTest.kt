package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.DiscoveryMapper
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import com.mefy.platemate.data.remote.rest.service.DiscoveryApiService
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoveryRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun getDiscoveryHome_usesCacheWhenForceRefreshIsFalse() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val api = FakeDiscoveryApiService()
        val repository = createRepository(api)

        val firstResult = repository.getDiscoveryHome(forceRefresh = false)
        val secondResult = repository.getDiscoveryHome(forceRefresh = false)

        assertTrue(firstResult is AppResult.Success)
        assertTrue(secondResult is AppResult.Success)
        assertEquals(1, api.callCount)
    }

    @Test
    fun getDiscoveryHome_forceRefreshBypassesCache() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val api = FakeDiscoveryApiService()
        val repository = createRepository(api)

        repository.getDiscoveryHome(forceRefresh = false)
        repository.getDiscoveryHome(forceRefresh = true)

        assertEquals(2, api.callCount)
    }

    private fun createRepository(api: DiscoveryApiService): DiscoveryRepositoryImpl = DiscoveryRepositoryImpl(
        api = api,
        mapper = DiscoveryMapper(),
        appDispatchers = AppDispatchers(
            main = mainDispatcherRule.dispatcher,
            io = mainDispatcherRule.dispatcher,
            default = mainDispatcherRule.dispatcher
        )
    )

    private class FakeDiscoveryApiService : DiscoveryApiService {
        var callCount = 0

        override suspend fun getDiscoveryHome(): DataResultResponse<DiscoveryHomeResponseDto> {
            callCount++
            return DataResultResponse(
                message = null,
                success = true,
                data = DiscoveryHomeResponseDto(
                    dailyStats = null,
                    tabs = null,
                    cityStats = null,
                    topCityPlates = null,
                    recentActivities = null
                )
            )
        }
    }
}

