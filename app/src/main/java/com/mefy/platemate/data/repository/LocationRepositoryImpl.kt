package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.UserLocationMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.websocket.datasource.SocketLocationDataSource
import com.mefy.platemate.data.remote.rest.service.LocationApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.location.UserLocation
import com.mefy.platemate.domain.repository.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApiService,
    private val socketLocationDataSource: SocketLocationDataSource,
    private val userLocationMapper: UserLocationMapper,
    private val appDispatchers: AppDispatchers
) : LocationRepository {

    override suspend fun getUserLocation(userId: Long): AppResult<UserLocation> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getUserLocation(userId) }.map(userLocationMapper::map)
        }

    override suspend fun getVisibleLocations(): AppResult<List<UserLocation>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getVisibleLocations() }.map(userLocationMapper::mapList)
        }

    override suspend fun blockUserFromLocation(targetUserId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.blockUserFromLocation(targetUserId) }
        }

    override suspend fun unblockUserFromLocation(targetUserId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.unblockUserFromLocation(targetUserId) }
        }

    override suspend fun getBlockedLocationUsers(): AppResult<List<Long>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getBlockedLocationUsers() }
        }

    override fun observeLocationUpdates(): Flow<UserLocation> =
        socketLocationDataSource.observeLocations().flowOn(appDispatchers.io)
}



