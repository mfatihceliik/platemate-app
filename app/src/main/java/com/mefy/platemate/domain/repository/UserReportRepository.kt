package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult

interface UserReportRepository {
    suspend fun reportUser(userId: Long, reason: String): AppResult<Unit>
}
