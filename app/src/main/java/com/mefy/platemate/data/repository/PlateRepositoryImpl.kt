package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateSearchResultMapper
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PlateRepositoryImpl @Inject constructor(
    private val api: PlateApiService,
    private val mapper: PlateSearchResultMapper,
    private val appDispatchers: AppDispatchers
) : PlateRepository {

    override suspend fun searchPlate(plateCode: String) =
        withContext(appDispatchers.io) {
            safeApiCall { api.searchPlate(plateCode) }.map(mapper::map)
        }
}
