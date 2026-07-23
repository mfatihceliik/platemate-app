package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.common.result.onSuccess
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.DiscoveryMapper
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.rest.service.DiscoveryApiService
import com.mefy.platemate.domain.model.discovery.CityPlatePage
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabFilter
import com.mefy.platemate.domain.model.discovery.DiscoveryTabPage
import com.mefy.platemate.domain.repository.DiscoveryRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class DiscoveryRepositoryImpl @Inject constructor(
    private val api: DiscoveryApiService,
    private val mapper: DiscoveryMapper,
    private val appDispatchers: AppDispatchers
) : DiscoveryRepository {

    @Volatile
    private var cachedHome: DiscoveryHome? = null

    override suspend fun getDiscoveryHome(forceRefresh: Boolean): AppResult<DiscoveryHome> =
        withContext(appDispatchers.io) {
            val cachedValue = cachedHome
            if (!forceRefresh && cachedValue != null) {
                return@withContext AppResult.Success(cachedValue)
            }

            safeApiCall { api.getDiscoveryHome() }
                .map(mapper::map)
                .onSuccess { mappedHome ->
                    cachedHome = mappedHome
                }
        }

    override suspend fun getCityPlates(cityId: Int, page: Int, size: Int): AppResult<CityPlatePage> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getCityPlates(cityId = cityId, page = page, size = size) }
                .map(mapper::mapCityPlatePage)
        }

    override suspend fun getTabFeed(
        tab: String,
        filter: DiscoveryTabFilter,
        page: Int,
        size: Int
    ): AppResult<DiscoveryTabPage> =
        withContext(appDispatchers.io) {
            safeApiCall {
                api.getDiscoveryTabPlates(
                    tabType = tab,
                    page = page,
                    size = size,
                    cityIds = filter.cityIds.takeIf { it.isNotEmpty() },
                    reportTypeCode = filter.reportTypeCode,
                    minRating = filter.minRating,
                    windowDays = filter.windowDays
                )
            }.map(mapper::mapTabPage)
        }

    override fun clearCache() {
        cachedHome = null
    }
}
