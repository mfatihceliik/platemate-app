package com.mefy.platemate.data.repository

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.rest.service.FollowApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class FollowRepositoryImpl @Inject constructor(
    private val api: FollowApiService,
    private val appDispatchers: AppDispatchers
) : FollowRepository {

    override suspend fun followUser(userId: Long) =
        withContext(appDispatchers.io) {
            safeResultCall { api.followUser(userId) }
        }

    override suspend fun unfollowUser(userId: Long) =
        withContext(appDispatchers.io) {
            safeResultCall { api.unfollowUser(userId) }
        }
}
