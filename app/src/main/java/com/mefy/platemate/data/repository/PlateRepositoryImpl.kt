package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateSearchResultMapper
import com.mefy.platemate.data.remote.dto.plate.AddPlateRemovalRequestRequest
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PlateRepositoryImpl @Inject constructor(
    private val api: PlateApiService,
    private val mapper: PlateSearchResultMapper,
    private val plateRemovalReasonMapper: com.mefy.platemate.data.mapper.PlateRemovalReasonMapper,
    private val appDispatchers: AppDispatchers
) : PlateRepository {

    override suspend fun searchPlate(plateCode: String) =
        withContext(appDispatchers.io) {
            safeApiCall { api.searchPlate(plateCode) }.map(mapper::map)
        }

    override suspend fun followPlate(plateCode: String) =
        withContext(appDispatchers.io) {
            safeResultCall { api.followPlate(plateCode) }
        }

    override suspend fun unfollowPlate(plateCode: String) =
        withContext(appDispatchers.io) {
            safeResultCall { api.unfollowPlate(plateCode) }
        }

    override suspend fun createRemovalRequest(plateId: Long, reasonCode: String, description: String) =
        withContext(appDispatchers.io) {
            safeApiCall {
                api.createRemovalRequest(
                    plateId,
                    AddPlateRemovalRequestRequest(reasonCode = reasonCode, description = description)
                )
            }.map { }
        }

    override suspend fun getPlateRemovalReasons() =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPlateRemovalReasons() }.map { dtos -> dtos.map(plateRemovalReasonMapper::map) }
        }
}
