package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.UserProfileMapper
import com.mefy.platemate.data.remote.dto.user.UpdateProfileRequest
import com.mefy.platemate.data.remote.rest.service.ProfileApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApiService,
    private val userProfileMapper: UserProfileMapper,
    private val appDispatchers: AppDispatchers
) : ProfileRepository {

    override suspend fun getProfile(userId: Long): AppResult<UserProfile> =
        withContext(appDispatchers.io) {
            safeApiCall {
                api.getProfile(userId = userId)
            }.map(userProfileMapper::map)
        }

    override suspend fun updateProfile(
        userId: Long,
        displayName: String?,
        username: String?,
        bio: String?,
        profilePhotoUrl: String?
    ): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall {
                api.updateProfile(userId, UpdateProfileRequest(displayName, username, bio, profilePhotoUrl))
            }
        }
}



