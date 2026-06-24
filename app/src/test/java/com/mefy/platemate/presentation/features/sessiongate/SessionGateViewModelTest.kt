package com.mefy.platemate.presentation.features.sessiongate

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.auth.RefreshSessionUseCase
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.features.auth.sessiongate.SessionGateTarget
import com.mefy.platemate.presentation.features.auth.sessiongate.SessionGateViewModel
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SessionGateViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun nullSession_setsAuthTarget() = runTest {
        val repository = FakeAuthRepository(
            initialSession = null,
            refreshResult = AppResult.Error(AppError.SessionExpired)
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SessionGateTarget.Auth, viewModel.uiState.value.target)
        assertEquals(0, repository.refreshCallCount)
    }

    @Test
    fun existingSession_refreshSuccess_setsMainTarget() = runTest {
        val repository = FakeAuthRepository(
            initialSession = sampleSession(),
            refreshResult = AppResult.Success(sampleSession(token = "new-token", refreshToken = "new-refresh"))
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SessionGateTarget.Main, viewModel.uiState.value.target)
        assertEquals(1, repository.refreshCallCount)
    }

    @Test
    fun existingSession_refreshUnauthorized_setsAuthTarget() = runTest {
        val repository = FakeAuthRepository(
            initialSession = sampleSession(),
            refreshResult = AppResult.Error(AppError.SessionExpired)
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SessionGateTarget.Auth, viewModel.uiState.value.target)
        assertEquals(1, repository.refreshCallCount)
    }

    @Test
    fun existingSession_refreshForbidden_setsAuthTarget() = runTest {
        val repository = FakeAuthRepository(
            initialSession = sampleSession(),
            refreshResult = AppResult.Error(AppError.SessionExpired)
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SessionGateTarget.Auth, viewModel.uiState.value.target)
        assertEquals(1, repository.refreshCallCount)
    }

    @Test
    fun existingSession_refreshNetworkFailure_setsMainTarget() = runTest {
        val repository = FakeAuthRepository(
            initialSession = sampleSession(),
            refreshResult = AppResult.Error(AppError.Network())
        )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(SessionGateTarget.Main, viewModel.uiState.value.target)
        assertEquals(1, repository.refreshCallCount)
    }

    private fun createViewModel(repository: FakeAuthRepository): SessionGateViewModel =
        SessionGateViewModel(
            observeSessionUseCase = ObserveSessionUseCase(repository),
            refreshSessionUseCase = RefreshSessionUseCase(repository),
            globalUiEventBus = DefaultGlobalUiEventBus()
        )

    private fun sampleSession(
        token: String = "token",
        refreshToken: String? = "refresh-token"
    ): AuthSession = AuthSession(
        userId = 7L,
        username = "fatih",
        token = token,
        refreshToken = refreshToken
    )

    private class FakeAuthRepository(
        initialSession: AuthSession?,
        private val refreshResult: AppResult<AuthSession>
    ) : AuthRepository {
        override val session: Flow<AuthSession?> = MutableStateFlow(initialSession)

        var refreshCallCount: Int = 0

        override suspend fun login(email: String, password: String): AppResult<AuthSession> =
            AppResult.Error(AppError.Server("Not used in this test"))

        override suspend fun register(
            username: String,
            email: String,
            password: String
        ): AppResult<AuthSession> =
            AppResult.Error(AppError.Server("Not used in this test"))

        override suspend fun refreshSession(): AppResult<AuthSession> {
            refreshCallCount++
            return refreshResult
        }

        override suspend fun logout() = Unit
    }
}
