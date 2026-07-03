package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult

interface FollowRepository {
    suspend fun followUser(userId: Long): AppResult<Unit>
    suspend fun unfollowUser(userId: Long): AppResult<Unit>
}
