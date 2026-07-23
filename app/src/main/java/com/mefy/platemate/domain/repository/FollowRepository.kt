package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.follow.FollowListItem

interface FollowRepository {
    suspend fun followUser(userId: Long): AppResult<Unit>
    suspend fun unfollowUser(userId: Long): AppResult<Unit>
    suspend fun getFollowers(userId: Long): AppResult<List<FollowListItem>>
    suspend fun getFollowing(userId: Long): AppResult<List<FollowListItem>>
}
