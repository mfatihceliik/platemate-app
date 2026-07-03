package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.SettingsOverviewMapper
import com.mefy.platemate.data.mapper.SocialMediaLinkMapper
import com.mefy.platemate.data.mapper.UserSettingsMapper
import com.mefy.platemate.data.remote.dto.settings.SettingsOverviewDto
import com.mefy.platemate.data.remote.dto.settings.UpdateSettingsRequest
import com.mefy.platemate.data.remote.dto.social.SocialMediaLinkDto
import com.mefy.platemate.data.remote.dto.user.UserSettingsDto
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
    fun getSettingsOverview_returnsUnauthorized_whenSessionMissing() = runTest {
        val api = FakeSettingsApiService()
        val repository = createRepository(api, initialSession = null)

        val result = repository.getSettingsOverview()

        assertEquals(AppResult.Error(AppError.SessionExpired), result)
    }

    @Test
    fun getSettingsOverview_mapsPayload() = runTest {
        val api = FakeSettingsApiService()
        val repository = createRepository(
            api = api,
            initialSession = AuthSession(userId = 88L, username = "fatih", token = "token")
        )

        val result = repository.getSettingsOverview()

        assertTrue(result is AppResult.Success)
        result as AppResult.Success
        assertEquals("mail@platemate.com", result.data.email)
        assertEquals(true, result.data.premiumActive)
        assertEquals(1, result.data.socialMediaLinks.size)
        assertEquals(88L, api.lastRequestedUserId)
    }

    @Test
    fun updateSettings_sendsThreeFlags() = runTest {
        val api = FakeSettingsApiService()
        val repository = createRepository(
            api = api,
            initialSession = AuthSession(userId = 88L, username = "fatih", token = "token")
        )

        val result = repository.updateSettings(
            UserSettings(
                messagingEnabled = true,
                onlineVisibilityEnabled = true,
                messageNotificationsEnabled = false,
                friendNotificationsEnabled = true
            )
        )

        assertTrue(result is AppResult.Success)
        assertEquals(
            UpdateSettingsRequest(
                messagingEnabled = true,
                onlineVisibilityEnabled = true,
                messageNotificationsEnabled = false,
                friendNotificationsEnabled = true
            ),
            api.lastUpdateRequest
        )
    }

    private fun createRepository(
        api: FakeSettingsApiService,
        initialSession: AuthSession?
    ): SettingsRepositoryImpl = SettingsRepositoryImpl(
        api = api,
        sessionStore = FakeSessionStore(initialSession),
        settingsOverviewMapper = SettingsOverviewMapper(
            userSettingsMapper = UserSettingsMapper(),
            socialMediaLinkMapper = SocialMediaLinkMapper()
        ),
        appDispatchers = AppDispatchers(
            main = mainDispatcherRule.dispatcher,
            io = mainDispatcherRule.dispatcher,
            default = mainDispatcherRule.dispatcher
        )
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
        var lastRequestedUserId: Long? = null
        var lastUpdateRequest: UpdateSettingsRequest? = null

        override suspend fun getSettingsOverview(userId: Long): DataResultResponse<SettingsOverviewDto> {
            lastRequestedUserId = userId
            return DataResultResponse(
                message = null,
                success = true,
                data = SettingsOverviewDto(
                    email = "mail@platemate.com",
                    premiumActive = true,
                    premiumUntil = "2026-06-30T00:00:00Z",
                    userSettings = UserSettingsDto(
                        messagingEnabled = true,
                        messageNotificationsEnabled = true,
                        friendNotificationsEnabled = false
                    ),
                    socialMediaLinks = listOf(
                        SocialMediaLinkDto(
                            id = 9L,
                            platformCode = "INSTAGRAM",
                            url = "https://instagram.com/test"
                        )
                    )
                )
            )
        }

        override suspend fun updateSettings(userId: Long, request: UpdateSettingsRequest): ResultResponse {
            lastUpdateRequest = request
            return ResultResponse(message = null, success = true)
        }
    }
}
