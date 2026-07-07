package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PremiumMapper
import com.mefy.platemate.data.remote.rest.service.PremiumApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.domain.model.premium.PremiumCatalog
import com.mefy.platemate.domain.repository.PremiumRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PremiumRepositoryImpl @Inject constructor(
    private val api: PremiumApiService,
    private val premiumMapper: PremiumMapper,
    private val appDispatchers: AppDispatchers
) : PremiumRepository {

    override suspend fun getCatalog(): AppResult<PremiumCatalog> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getCatalog() }.map(premiumMapper::map)
        }
}
