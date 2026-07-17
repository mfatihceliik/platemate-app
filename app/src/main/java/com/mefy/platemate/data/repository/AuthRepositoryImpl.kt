package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.flatMap
import com.mefy.platemate.core.common.result.onSuccessSuspend
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.UserAuthSessionMapper
import com.mefy.platemate.data.remote.rest.service.AuthApiService
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.data.remote.dto.auth.ChangePasswordRequest
import com.mefy.platemate.data.remote.dto.auth.LoginRequest
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.dto.auth.RegisterRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.repository.FcmTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val authTokenApiService: AuthTokenApiService,
    private val sessionStore: SessionStore,
    private val userAuthSessionMapper: UserAuthSessionMapper,
    private val fcmTokenRepository: FcmTokenRepository,
    private val appDispatchers: AppDispatchers
) : AuthRepository {

    override val session: Flow<AuthSession?> = sessionStore.session

    override suspend fun login(email: String, password: String): AppResult<AuthSession> =
        withContext(appDispatchers.io) {
            safeApiCall { api.login(LoginRequest.fromIdentifier(email, password)) }
                .flatMap { user ->
                    userAuthSessionMapper.mapOrNull(user)
                        ?.let { session -> AppResult.Success(session) }
                        ?: AppResult.Error(AppError.Api("Access or refresh token not found in login response"))
                }
                .onSuccessSuspend(sessionStore::saveSession)
        }

    override suspend fun register(username: String, email: String, password: String): AppResult<AuthSession> =
        withContext(appDispatchers.io) {
            safeApiCall { api.register(RegisterRequest(username, password, email)) }
                .flatMap { user ->
                    userAuthSessionMapper.mapOrNull(user)
                        ?.let { session -> AppResult.Success(session) }
                        ?: AppResult.Error(AppError.Api("Access or refresh token not found in register response"))
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
                val newAccessToken = user.token?.takeIf { it.isNotBlank() }
                val newRefreshToken = user.refreshToken?.takeIf { it.isNotBlank() }
                
                if (newAccessToken != null && newRefreshToken != null) {
                    val currentSession = sessionStore.session.first()
                    if (currentSession != null) {
                        val refreshedSession = currentSession.copy(
                            token = newAccessToken,
                            refreshToken = newRefreshToken
                        )
                        AppResult.Success(refreshedSession)
                    } else {
                        userAuthSessionMapper.mapOrNull(user)
                            ?.let { session -> AppResult.Success(session) }
                            ?: AppResult.Error(AppError.Api("Access or refresh token not found in refresh response"))
                    }
                } else {
                    AppResult.Error(AppError.Api("Access or refresh token not found in refresh response"))
                }
            }.onSuccessSuspend(sessionStore::saveSession)

            if (refreshResult is AppResult.Error) {
                val shouldClearSession = refreshResult.error is AppError.SessionExpired
                if (shouldClearSession) {
                    sessionStore.clearSession()
                }
            }

            refreshResult
        }

    override suspend fun changePassword(currentPassword: String, newPassword: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.changePassword(ChangePasswordRequest(currentPassword, newPassword)) }
        }

    override suspend fun logout() {
        withContext(appDispatchers.io) {
            // Unregister the FCM token while the session is still valid (request is authenticated),
            // so a logged-out device no longer receives push notifications.
            runCatching {
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                if (fcmToken.isNotBlank()) {
                    fcmTokenRepository.unregisterToken(fcmToken)
                }
            }

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

