package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.data.remote.dto.user.UserRoleCode
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.domain.model.auth.AuthSession
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse

class TokenAuthenticatorTest {

    private val refreshFailureClassifier = RefreshFailureClassifier()

    @Test
    fun authenticate_refreshSuccess_retriesWithNewAccessTokenAndUpdatesSession() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = "old-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService {
            DataResultResponse(
                message = null,
                success = true,
                data = refreshedUser(token = "new-access", refreshToken = "new-refresh")
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNotNull(retryRequest)
        assertEquals("Bearer new-access", retryRequest!!.header("Authorization"))
        assertEquals(1, refreshApi.refreshCallCount.get())
        val updatedSession = sessionStore.session.first()
        assertEquals("new-access", updatedSession?.token)
        assertEquals("new-refresh", updatedSession?.refreshToken)
    }

    @Test
    fun authenticate_refreshUnauthorizedWithInvalidCode_clearsSessionAndReturnsNull() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = "old-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService {
            throw HttpException(
                RetrofitResponse.error<String>(
                    401,
                    """
                    {
                      "success": false,
                      "message": "refresh expired",
                      "data": { "code": "REFRESH_EXPIRED" }
                    }
                    """.trimIndent().toResponseBody(null)
                )
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNull(retryRequest)
        assertNull(sessionStore.session.first())
    }

    @Test
    fun authenticate_refreshUnauthorizedWithUnknownCode_clearsSessionAndReturnsNull() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = "old-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService {
            throw HttpException(
                RetrofitResponse.error<String>(
                    401,
                    """
                    {
                      "success": false,
                      "message": "refresh failed",
                      "data": { "code": "UNKNOWN_REASON" }
                    }
                    """.trimIndent().toResponseBody(null)
                )
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNull(retryRequest)
        assertNull(sessionStore.session.first())
    }

    @Test
    fun authenticate_refreshNetworkFailure_keepsSessionAndReturnsNull() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = "old-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService { throw IOException("offline") }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNull(retryRequest)
        assertNotNull(sessionStore.session.first())
        assertEquals("old-access", sessionStore.session.first()?.token)
        assertEquals("old-refresh", sessionStore.session.first()?.refreshToken)
    }

    @Test
    fun authenticate_missingLegacyRefreshToken_clearsSessionAndReturnsNull() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = null
            )
        )
        val refreshApi = FakeAuthTokenApiService {
            DataResultResponse(
                message = null,
                success = true,
                data = refreshedUser(token = "new-access", refreshToken = "new-refresh")
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNull(retryRequest)
        assertNull(sessionStore.session.first())
        assertEquals(0, refreshApi.refreshCallCount.get())
    }

    @Test
    fun authenticate_whenTokenAlreadyRotated_retriesWithLatestTokenWithoutRefresh() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "new-access",
                refreshToken = "new-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService {
            DataResultResponse(
                message = null,
                success = true,
                data = refreshedUser(token = "unused-access", refreshToken = "unused-refresh")
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val retryRequest = authenticator.authenticate(
            route = null,
            response = unauthorizedResponse(token = "old-access")
        )

        assertNotNull(retryRequest)
        assertEquals("Bearer new-access", retryRequest!!.header("Authorization"))
        assertEquals(0, refreshApi.refreshCallCount.get())
    }

    @Test
    fun authenticate_parallel401Requests_triggersSingleRefreshCall() = runBlocking {
        val sessionStore = FakeSessionStore(
            AuthSession(
                userId = 1L,
                username = "mfy",
                token = "old-access",
                refreshToken = "old-refresh"
            )
        )
        val refreshApi = FakeAuthTokenApiService(delayMillis = 150L) {
            DataResultResponse(
                message = null,
                success = true,
                data = refreshedUser(token = "new-access", refreshToken = "new-refresh")
            )
        }
        val authenticator = TokenAuthenticator(sessionStore, refreshApi, refreshFailureClassifier)

        val executor = Executors.newFixedThreadPool(2)
        val doneSignal = CountDownLatch(2)
        val firstResult = arrayOfNulls<Request>(1)
        val secondResult = arrayOfNulls<Request>(1)

        executor.execute {
            firstResult[0] = authenticator.authenticate(null, unauthorizedResponse("old-access"))
            doneSignal.countDown()
        }
        executor.execute {
            secondResult[0] = authenticator.authenticate(null, unauthorizedResponse("old-access"))
            doneSignal.countDown()
        }

        assertTrue(doneSignal.await(2, TimeUnit.SECONDS))
        executor.shutdownNow()

        assertEquals(1, refreshApi.refreshCallCount.get())
        assertEquals("Bearer new-access", firstResult[0]?.header("Authorization"))
        assertEquals("Bearer new-access", secondResult[0]?.header("Authorization"))
        assertEquals("new-access", sessionStore.session.first()?.token)
    }

    private fun unauthorizedResponse(
        token: String,
        path: String = "/api/profiles/1"
    ): Response {
        val request = Request.Builder()
            .url("http://localhost$path")
            .header("Authorization", "Bearer $token")
            .build()
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()
    }

    private fun refreshedUser(token: String, refreshToken: String): UserDto = UserDto(
        id = 1L,
        username = "mfy",
        email = "mfy@test.com",
        token = token,
        refreshToken = refreshToken,
        premiumUntil = null,
        premiumActive = false,
        roleCode = UserRoleCode.NORMAL,
        currentSubscriptionStartedAt = null,
        currentSubscriptionExpiresAt = null,
        currentSubscriptionPurchasedDays = null,
        currentSubscriptionStatus = null
    )

    private class FakeSessionStore(initialSession: AuthSession?) : SessionStore {
        private val state = MutableStateFlow(initialSession)
        override val session: Flow<AuthSession?> = state

        override suspend fun saveSession(session: AuthSession) {
            state.value = session
        }

        override suspend fun clearSession() {
            state.value = null
        }

        override suspend fun getToken(): String? = state.value?.token

        override fun peekToken(): String? = state.value?.token

        override suspend fun getRefreshToken(): String? = state.value?.refreshToken

        override fun peekRefreshToken(): String? = state.value?.refreshToken
    }

    private class FakeAuthTokenApiService(
        private val delayMillis: Long = 0L,
        private val refreshHandler: suspend (RefreshTokenRequest) -> DataResultResponse<UserDto>
    ) : AuthTokenApiService {

        val refreshCallCount = AtomicInteger(0)
        val logoutCallCount = AtomicInteger(0)

        override suspend fun refresh(request: RefreshTokenRequest): DataResultResponse<UserDto> {
            refreshCallCount.incrementAndGet()
            if (delayMillis > 0L) {
                Thread.sleep(delayMillis)
            }
            return refreshHandler(request)
        }

        override suspend fun logout(request: RefreshTokenRequest): ResultResponse {
            logoutCallCount.incrementAndGet()
            return ResultResponse(success = true, message = null)
        }
    }
}
