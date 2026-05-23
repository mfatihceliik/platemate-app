package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.common.onSuccess
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.DiscoveryMapper
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.rest.service.DiscoveryApiService
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
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
}
