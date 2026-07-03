package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult

interface UserBlockRepository {
    suspend fun blockUser(userId: Long): AppResult<Unit>
    suspend fun unblockUser(userId: Long): AppResult<Unit>
}
