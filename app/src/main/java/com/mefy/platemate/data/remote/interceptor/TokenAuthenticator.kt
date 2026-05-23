package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.domain.model.auth.AuthSession
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException

@Singleton
class TokenAuthenticator @Inject constructor(
    private val sessionStore: SessionStore,
    private val authTokenApiService: AuthTokenApiService,
    private val refreshFailureClassifier: RefreshFailureClassifier
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != HTTP_UNAUTHORIZED) return null
        if (AuthPathPolicy.shouldBypassRefresh(response.request.url.encodedPath)) return null
        if (responseCount(response) >= MAX_AUTH_RETRIES) return null

        val failedRequest = response.request
        val failedRequestToken = failedRequest.header(HEADER_AUTHORIZATION).toBearerTokenOrNull()
        val latestAccessToken = sessionStore.peekToken()
        if (!latestAccessToken.isNullOrBlank() && failedRequestToken != latestAccessToken) {
            return failedRequest.withBearerToken(latestAccessToken)
        }

        synchronized(refreshLock) {
            val currentSession = runBlocking { sessionStore.session.first() } ?: return null
            if (failedRequestToken != currentSession.token) {
                return failedRequest.withBearerToken(currentSession.token)
            }

            val refreshToken = currentSession.refreshToken?.takeIf { it.isNotBlank() } ?: run {
                runBlocking { sessionStore.clearSession() }
                return null
            }

            val refreshedSession = refreshSessionTokens(currentSession, refreshToken) ?: return null
            return failedRequest.withBearerToken(refreshedSession.token)
        }
    }

    private fun refreshSessionTokens(
        currentSession: AuthSession,
        refreshToken: String
    ): AuthSession? {
        val response = try {
            runBlocking {
                authTokenApiService.refresh(RefreshTokenRequest(refreshToken))
            }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                val statusCode = throwable.code()
                val rawBody = throwable.response()?.errorBody()?.string()
                if (refreshFailureClassifier.shouldClearSession(statusCode = statusCode, rawBody = rawBody)) {
                    runBlocking { sessionStore.clearSession() }
                }
            }
            return null
        }

        if (!response.success) {
            return null
        }

        val refreshedUser = response.data ?: return null
        val refreshedAccessToken = refreshedUser.token?.takeIf { it.isNotBlank() } ?: return null
        val refreshedRefreshToken = refreshedUser.refreshToken?.takeIf { it.isNotBlank() } ?: return null

        val refreshedSession = currentSession.copy(
            token = refreshedAccessToken,
            refreshToken = refreshedRefreshToken
        )
        runBlocking { sessionStore.saveSession(refreshedSession) }
        return refreshedSession
    }

    private fun Request.withBearerToken(token: String): Request =
        newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $token")
            .build()

    private fun String?.toBearerTokenOrNull(): String? {
        if (this.isNullOrBlank()) return null
        val bearerPrefix = "Bearer "
        return if (startsWith(bearerPrefix)) removePrefix(bearerPrefix).trim() else null
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var currentResponse = response
        while (currentResponse.priorResponse != null) {
            count++
            currentResponse = currentResponse.priorResponse!!
        }
        return count
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HTTP_UNAUTHORIZED = 401
        const val MAX_AUTH_RETRIES = 2

        val refreshLock = Any()
    }
}
