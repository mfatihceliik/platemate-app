package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.UserAuthSessionMapper
import com.mefy.platemate.data.remote.dto.auth.ChangePasswordRequest
import com.mefy.platemate.data.remote.dto.auth.LoginRequest
import com.mefy.platemate.data.remote.dto.auth.RefreshTokenRequest
import com.mefy.platemate.data.remote.dto.auth.RegisterRequest
import com.mefy.platemate.data.remote.dto.user.UserDto
import com.mefy.platemate.data.remote.dto.user.UserRoleCode
import com.mefy.platemate.data.remote.rest.service.AuthApiService
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.FcmTokenRepository
import com.mefy.platemate.testutil.MainDispatcherRule
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun refreshSession_success_updatesStoredSession() = runTest {
        val sessionStore = FakeSessionStore(sampleSession())
        val authTokenApi = FakeAuthTokenApiService(
            refreshHandler = {
                DataResultResponse(
                    success = true,
                    message = null,
                    data = sampleUserDto(token = "new-access", refreshToken = "new-refresh")
                )
            }
        )
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        val result = repository.refreshSession()

        assertTrue(result is AppResult.Success)
        assertEquals(1, authTokenApi.refreshCallCount)
        assertEquals("new-access", sessionStore.session.first()?.token)
        assertEquals("new-refresh", sessionStore.session.first()?.refreshToken)
    }

    @Test
    fun refreshSession_withoutRefreshToken_clearsSessionAndReturnsUnauthorized() = runTest {
        val sessionStore = FakeSessionStore(sampleSession(refreshToken = null))
        val authTokenApi = FakeAuthTokenApiService()
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        val result = repository.refreshSession()

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.SessionExpired, (result as AppResult.Error).error)
        assertNull(sessionStore.session.first())
        assertEquals(0, authTokenApi.refreshCallCount)
    }

    @Test
    fun refreshSession_unauthorizedFromBackend_clearsSession() = runTest {
        val sessionStore = FakeSessionStore(sampleSession())
        val authTokenApi = FakeAuthTokenApiService(
            refreshHandler = {
                throw HttpException(
                    RetrofitResponse.error<String>(
                        401,
                        "{\"success\":false,\"message\":\"unauthorized\"}".toResponseBody(null)
                    )
                )
            }
        )
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        val result = repository.refreshSession()

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.SessionExpired, (result as AppResult.Error).error)
        assertNull(sessionStore.session.first())
        assertEquals(1, authTokenApi.refreshCallCount)
    }

    @Test
    fun refreshSession_networkError_keepsSessionForOfflineTolerance() = runTest {
        val sessionStore = FakeSessionStore(sampleSession())
        val authTokenApi = FakeAuthTokenApiService(
            refreshHandler = { throw IOException("offline") }
        )
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        val result = repository.refreshSession()

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
        assertEquals("access", sessionStore.session.first()?.token)
        assertEquals(1, authTokenApi.refreshCallCount)
    }

    @Test
    fun logout_withRefreshToken_callsBackendLogoutAndClearsSession() = runTest {
        val sessionStore = FakeSessionStore(sampleSession())
        val authTokenApi = FakeAuthTokenApiService()
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        repository.logout()

        assertEquals(1, authTokenApi.logoutCallCount)
        assertEquals("refresh-token", authTokenApi.lastLogoutRefreshToken)
        assertNull(sessionStore.session.first())
    }

    @Test
    fun logout_whenBackendFails_stillClearsSession() = runTest {
        val sessionStore = FakeSessionStore(sampleSession())
        val authTokenApi = FakeAuthTokenApiService(throwOnLogout = true)
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        repository.logout()

        assertEquals(1, authTokenApi.logoutCallCount)
        assertNull(sessionStore.session.first())
    }

    @Test
    fun logout_withoutRefreshToken_skipsBackendAndClearsSession() = runTest {
        val sessionStore = FakeSessionStore(sampleSession(refreshToken = null))
        val authTokenApi = FakeAuthTokenApiService()
        val repository = createRepository(sessionStore = sessionStore, authTokenApi = authTokenApi)

        repository.logout()

        assertEquals(0, authTokenApi.logoutCallCount)
        assertNull(sessionStore.session.first())
    }

    private fun createRepository(
        sessionStore: FakeSessionStore,
        authTokenApi: FakeAuthTokenApiService
    ): AuthRepositoryImpl = AuthRepositoryImpl(
        api = FakeAuthApiService(),
        authTokenApiService = authTokenApi,
        sessionStore = sessionStore,
        userAuthSessionMapper = UserAuthSessionMapper(),
        fcmTokenRepository = FakeFcmTokenRepository(),
        appDispatchers = testDispatchers()
    )

    private class FakeFcmTokenRepository : FcmTokenRepository {
        override suspend fun registerToken(token: String): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun unregisterToken(token: String): AppResult<Unit> = AppResult.Success(Unit)
    }

    private fun testDispatchers(): AppDispatchers = AppDispatchers(
        main = mainDispatcherRule.dispatcher,
        io = mainDispatcherRule.dispatcher,
        default = mainDispatcherRule.dispatcher
    )

    private fun sampleSession(refreshToken: String? = "refresh-token"): AuthSession = AuthSession(
        userId = 1L,
        username = "fatih",
        token = "access",
        refreshToken = refreshToken
    )

    private fun sampleUserDto(token: String, refreshToken: String): UserDto = UserDto(
        id = 1L,
        username = "fatih",
        email = "fatih@test.com",
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

    private class FakeAuthApiService : AuthApiService {
        override suspend fun register(request: RegisterRequest): DataResultResponse<UserDto> {
            throw UnsupportedOperationException("Not used in this test")
        }

        override suspend fun login(request: LoginRequest): DataResultResponse<UserDto> {
            throw UnsupportedOperationException("Not used in this test")
        }

        override suspend fun changePassword(request: ChangePasswordRequest): ResultResponse {
            throw UnsupportedOperationException("Not used in this test")
        }
    }

    private class FakeAuthTokenApiService(
        private val throwOnLogout: Boolean = false,
        private val refreshHandler: suspend (RefreshTokenRequest) -> DataResultResponse<UserDto> = {
            throw UnsupportedOperationException("Not used in this test")
        }
    ) : AuthTokenApiService {
        var refreshCallCount: Int = 0
        var logoutCallCount: Int = 0
        var lastLogoutRefreshToken: String? = null

        override suspend fun refresh(request: RefreshTokenRequest): DataResultResponse<UserDto> {
            refreshCallCount++
            return refreshHandler(request)
        }

        override suspend fun logout(request: RefreshTokenRequest): ResultResponse {
            logoutCallCount++
            lastLogoutRefreshToken = request.refreshToken
            if (throwOnLogout) {
                throw IllegalStateException("backend unavailable")
            }
            return ResultResponse(success = true, message = null)
        }
    }
}
