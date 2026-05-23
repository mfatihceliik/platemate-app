package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.social.Friendship

interface SocialRepository {
    suspend fun sendFriendRequest(addresseeId: Long): AppResult<Unit>
    suspend fun acceptFriendRequest(id: Long): AppResult<Unit>
    suspend fun rejectFriendRequest(id: Long): AppResult<Unit>
    suspend fun removeFriend(id: Long): AppResult<Unit>
    suspend fun getFriends(): AppResult<List<Friendship>>
    suspend fun getPendingRequests(): AppResult<List<Friendship>>
}

