package com.mefy.platemate.data.repository

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.UserSettingsMapper
import com.mefy.platemate.data.remote.dto.user.UserSettingsDto
import com.mefy.platemate.data.remote.dto.settings.UpdateSettingsRequest
import com.mefy.platemate.data.remote.rest.service.SettingsApiService
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun getMySettings_returnsUnauthorized_whenSessionMissing() = runTest {
        val api = FakeSettingsApiService()
        val repository = SettingsRepositoryImpl(
            api = api,
            sessionStore = FakeSessionStore(initialSession = null),
            userSettingsMapper = UserSettingsMapper(),
            appDispatchers = testDispatchers()
        )

        val result = repository.getMySettings()

        assertEquals(AppResult.Error(AppError.Unauthorized), result)
        assertEquals(0, api.getMySettingsCallCount)
    }

    @Test
    fun getMySettings_usesSessionUserId_whenSessionExists() = runTest {
        val api = FakeSettingsApiService()
        val repository = SettingsRepositoryImpl(
            api = api,
            sessionStore = FakeSessionStore(
                initialSession = AuthSession(
                    userId = 88L,
                    username = "fatih",
                    token = "token"
                )
            ),
            userSettingsMapper = UserSettingsMapper(),
            appDispatchers = testDispatchers()
        )

        val result = repository.getMySettings()

        assertTrue(result is AppResult.Success)
        result as AppResult.Success
        assertEquals(
            UserSettings(
                messagingEnabled = true,
                locationSharingEnabled = false,
                messageNotificationsEnabled = true,
                friendNotificationsEnabled = false
            ),
            result.data
        )
        assertEquals(88L, api.lastRequestedUserId)
    }

    private fun testDispatchers(): AppDispatchers = AppDispatchers(
        main = mainDispatcherRule.dispatcher,
        io = mainDispatcherRule.dispatcher,
        default = mainDispatcherRule.dispatcher
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

    private class FakeSettingsApiService : SettingsApiService {
        var getMySettingsCallCount: Int = 0
        var lastRequestedUserId: Long? = null

        override suspend fun getMySettings(userId: Long): DataResultResponse<UserSettingsDto> {
            getMySettingsCallCount++
            lastRequestedUserId = userId
            return DataResultResponse(
                success = true,
                message = null,
                data = UserSettingsDto(
                    messagingEnabled = true,
                    locationSharingEnabled = false,
                    messageNotificationsEnabled = true,
                    friendNotificationsEnabled = false
                )
            )
        }

        override suspend fun updateSettings(
            userId: Long,
            request: UpdateSettingsRequest
        ): ResultResponse = ResultResponse(success = true, message = null)
    }
}

