package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult

interface FcmTokenRepository {
    suspend fun registerToken(token: String): AppResult<Unit>
    suspend fun unregisterToken(token: String): AppResult<Unit>
}
