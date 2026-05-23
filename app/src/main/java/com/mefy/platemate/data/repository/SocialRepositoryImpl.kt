package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.FriendshipMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.rest.service.SocialApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.social.Friendship
import com.mefy.platemate.domain.repository.SocialRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class SocialRepositoryImpl @Inject constructor(
    private val api: SocialApiService,
    private val friendshipMapper: FriendshipMapper,
    private val appDispatchers: AppDispatchers
) : SocialRepository {

    override suspend fun sendFriendRequest(addresseeId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.sendFriendRequest(addresseeId) }
        }

    override suspend fun acceptFriendRequest(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.acceptFriendRequest(id) }
        }

    override suspend fun rejectFriendRequest(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.rejectFriendRequest(id) }
        }

    override suspend fun removeFriend(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.removeFriend(id) }
        }

    override suspend fun getFriends(): AppResult<List<Friendship>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getMyFriends() }.map(friendshipMapper::mapList)
        }

    override suspend fun getPendingRequests(): AppResult<List<Friendship>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPendingRequests() }.map(friendshipMapper::mapList)
        }
}


