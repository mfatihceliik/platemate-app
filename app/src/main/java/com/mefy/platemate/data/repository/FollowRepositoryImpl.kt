package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.mapper.FollowListItemMapper
import com.mefy.platemate.data.remote.rest.service.FollowApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.FollowRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class FollowRepositoryImpl @Inject constructor(
    private val api: FollowApiService,
    private val followListItemMapper: FollowListItemMapper,
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

    override suspend fun getFollowers(userId: Long) =
        withContext(appDispatchers.io) {
            safeApiCall { api.getFollowers(userId) }.map(followListItemMapper::mapList)
        }

    override suspend fun getFollowing(userId: Long) =
        withContext(appDispatchers.io) {
            safeApiCall { api.getFollowing(userId) }.map(followListItemMapper::mapList)
        }
}
