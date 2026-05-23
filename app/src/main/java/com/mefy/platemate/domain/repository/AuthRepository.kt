package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.auth.AuthSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<AuthSession?>
    suspend fun login(email: String, password: String): AppResult<AuthSession>
    suspend fun register(username: String, email: String, password: String): AppResult<AuthSession>
    suspend fun refreshSession(): AppResult<AuthSession>
    suspend fun logout()
}

