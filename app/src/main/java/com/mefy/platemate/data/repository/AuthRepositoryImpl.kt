package com.mefy.platemate.data.repository

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.flatMap
import com.mefy.platemate.core.common.onSuccessSuspend
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.UserAuthSessionMapper
import com.mefy.platemate.data.remote.rest.service.AuthApiService
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.data.remote.dto.auth.LoginRequest
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.dto.auth.RegisterRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val authTokenApiService: AuthTokenApiService,
    private val sessionStore: SessionStore,
    private val userAuthSessionMapper: UserAuthSessionMapper,
    private val appDispatchers: AppDispatchers
) : AuthRepository {

    override val session: Flow<AuthSession?> = sessionStore.session

    override suspend fun login(email: String, password: String): AppResult<AuthSession> =
        withContext(appDispatchers.io) {
            safeApiCall { api.login(LoginRequest.fromIdentifier(email, password)) }
                .flatMap { user ->
                    userAuthSessionMapper.mapOrNull(user)
                        ?.let { session -> AppResult.Success(session) }
                        ?: AppResult.Error(AppError.Server("Access or refresh token not found in login response"))
                }
                .onSuccessSuspend(sessionStore::saveSession)
        }

    override suspend fun register(username: String, email: String, password: String): AppResult<AuthSession> =
        withContext(appDispatchers.io) {
            safeApiCall { api.register(RegisterRequest(username, password, email)) }
                .flatMap { user ->
                    userAuthSessionMapper.mapOrNull(user)
                        ?.let { session -> AppResult.Success(session) }
                        ?: AppResult.Error(AppError.Server("Access or refresh token not found in register response"))
                }
                .onSuccessSuspend(sessionStore::saveSession)
        }

    override suspend fun refreshSession(): AppResult<AuthSession> =
        withContext(appDispatchers.io) {
            val refreshToken = sessionStore.peekRefreshToken()?.takeIf { it.isNotBlank() }
                ?: sessionStore.getRefreshToken()?.takeIf { it.isNotBlank() }

            if (refreshToken.isNullOrBlank()) {
                sessionStore.clearSession()
                return@withContext AppResult.Error(AppError.SessionExpired)
            }

            val refreshResult = safeApiCall {
                authTokenApiService.refresh(RefreshTokenRequest(refreshToken))
            }.flatMap { user ->
                userAuthSessionMapper.mapOrNull(user)
                    ?.let { session -> AppResult.Success(session) }
                    ?: AppResult.Error(AppError.Server("Access or refresh token not found in refresh response"))
            }.onSuccessSuspend(sessionStore::saveSession)

            if (refreshResult is AppResult.Error) {
                val shouldClearSession = refreshResult.error is AppError.SessionExpired
                if (shouldClearSession) {
                    sessionStore.clearSession()
                }
            }

            refreshResult
        }

    override suspend fun logout() {
        withContext(appDispatchers.io) {
            val refreshToken = sessionStore.peekRefreshToken()?.takeIf { it.isNotBlank() }
                ?: sessionStore.getRefreshToken()?.takeIf { it.isNotBlank() }

            if (!refreshToken.isNullOrBlank()) {
                runCatching {
                    authTokenApiService.logout(RefreshTokenRequest(refreshToken))
                }
            }

            sessionStore.clearSession()
        }
    }
}

