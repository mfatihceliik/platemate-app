package com.mefy.platemate.data.local

import com.mefy.platemate.domain.model.auth.AuthSession
import kotlinx.coroutines.flow.Flow

interface SessionStore {
    val session: Flow<AuthSession?>
    suspend fun saveSession(session: AuthSession)
    suspend fun clearSession()
    suspend fun getToken(): String?
    fun peekToken(): String?
    suspend fun getRefreshToken(): String?
    fun peekRefreshToken(): String?
}

