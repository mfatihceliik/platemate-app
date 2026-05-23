package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.location.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getUserLocation(userId: Long): AppResult<UserLocation>
    suspend fun getVisibleLocations(): AppResult<List<UserLocation>>
    suspend fun blockUserFromLocation(targetUserId: Long): AppResult<Unit>
    suspend fun unblockUserFromLocation(targetUserId: Long): AppResult<Unit>
    suspend fun getBlockedLocationUsers(): AppResult<List<Long>>
    fun observeLocationUpdates(): Flow<UserLocation>
}


