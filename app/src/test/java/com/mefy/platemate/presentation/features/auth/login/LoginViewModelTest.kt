package com.mefy.platemate.presentation.features.auth.login

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.usecase.auth.LoginUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
import com.mefy.platemate.presentation.common.error.DefaultUiErrorResolver
import com.mefy.platemate.presentation.common.event.CommonUiEvent
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.login.reducer.LoginStateReducer
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadingState_preventsSecondSubmitWhileFirstRequestIsRunning() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Success(sampleSession()),
            loginDelayMs = 1_000L
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        viewModel.onAction(LoginUiAction.SubmitClicked)
        viewModel.onAction(LoginUiAction.SubmitClicked)

        assertTrue(viewModel.uiState.value.submitState is UiActionState.Loading)

        runCurrent()
        assertEquals(1, repository.loginCallCount)

        advanceUntilIdle()
    }

    @Test
    fun errorResult_setsErrorState_mapsEmailAndPasswordErrors_andEmitsSnackbar() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.Backend(
                    message = "Login failed",
                    fieldErrors = mapOf(
                        "identifier" to "Use email or username",
                        "email" to "Email format is invalid",
                        "password" to "Password is incorrect"
                    )
                )
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.commonUiEvents.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val expectedMessage = UiText.Resource(R.string.common_error_unknown)
        assertTrue(state.submitState is UiActionState.Error)
        assertEquals("Email format is invalid", state.emailError)
        assertEquals("Password is incorrect", state.passwordError)
        assertEquals(expectedMessage, state.formMessage)
        assertEquals(
            CommonUiEvent.ShowSnackbar(expectedMessage),
            emittedEvent.await()
        )
    }

    @Test
    fun serverUnavailableError_setsResourceMessage_andEmitsSnackbar() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.ServerUnavailable(message = "Connection refused")
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.commonUiEvents.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val expectedMessage = UiText.Resource(R.string.common_error_server_unavailable)
        val state = viewModel.uiState.value
        assertTrue(state.submitState is UiActionState.Error)
        assertEquals(expectedMessage, state.formMessage)
        assertEquals(CommonUiEvent.ShowSnackbar(expectedMessage), emittedEvent.await())
    }

    @Test
    fun unauthorizedError_showsInvalidCredentialsMessage_andEmitsSnackbar() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(AppError.Unauthorized)
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.commonUiEvents.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val expectedMessage = UiText.Resource(R.string.auth_login_invalid_credentials)
        val state = viewModel.uiState.value
        assertTrue(state.submitState is UiActionState.Error)
        assertEquals(expectedMessage, state.formMessage)
        assertEquals(CommonUiEvent.ShowSnackbar(expectedMessage), emittedEvent.await())
    }

    @Test
    fun successResult_resetsSubmitStateAndEmitsNavigateEffect() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEffect = async { viewModel.uiEffect.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.submitState is UiActionState.Idle)
        assertNull(state.formMessage)
        assertEquals(LoginUiEffect.NavigateAfterLogin, emittedEffect.await())
    }

    @Test
    fun invalidEmail_blocksSubmitAndSkipsRepositoryCall() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(LoginUiAction.EmailChanged("invalid-email"))
        viewModel.onAction(LoginUiAction.PasswordChanged("123456"))
        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isEmailFormatValid)
        assertFalse(state.isSubmitEnabled)
        assertNotNull(state.formMessage)
        assertEquals(0, repository.loginCallCount)
    }

    @Test
    fun emptyPassword_blocksSubmitAndSkipsRepositoryCall() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(LoginUiAction.EmailChanged("fatih@test.com"))
        viewModel.onAction(LoginUiAction.PasswordChanged(""))
        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSubmitEnabled)
        assertNotNull(state.formMessage)
        assertEquals(0, repository.loginCallCount)
    }

    @Test
    fun emailChange_clearsEmailErrorAndFormMessage() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.Backend(
                    message = "Login failed",
                    fieldErrors = mapOf(
                        "email" to "Email format is invalid",
                        "password" to "Password is incorrect"
                    )
                )
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        viewModel.onAction(LoginUiAction.EmailChanged("new-email@test.com"))
        val state = viewModel.uiState.value

        assertNull(state.emailError)
        assertNull(state.formMessage)
    }

    @Test
    fun submitAttempt_setsHasSubmittedOnceTrue_whenFormIsInvalid() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.hasSubmittedOnce)
        assertFalse(state.isSubmitEnabled)
        assertNotNull(state.formMessage)
        assertEquals(0, repository.loginCallCount)
    }

    private fun createViewModel(repository: FakeAuthRepository): LoginViewModel =
        LoginViewModel(
            loginUseCase = LoginUseCase(repository),
            loginStateReducer = LoginStateReducer(
                validateLoginFormUseCase = ValidateLoginFormUseCase(ValidateEmailFormatUseCase())
            ),
            uiErrorResolver = DefaultUiErrorResolver()
        )

    private fun fillValidForm(viewModel: LoginViewModel) {
        viewModel.onAction(LoginUiAction.EmailChanged("fatih@test.com"))
        viewModel.onAction(LoginUiAction.PasswordChanged("123456"))
    }

    private fun sampleSession(): AuthSession =
        AuthSession(userId = 1L, username = "fatih", token = "token")

    private class FakeAuthRepository(
        var loginResult: AppResult<AuthSession>,
        var loginDelayMs: Long = 0L
    ) : AuthRepository {

        override val session: Flow<AuthSession?> = MutableStateFlow(null)
        var loginCallCount: Int = 0

        override suspend fun login(email: String, password: String): AppResult<AuthSession> {
            loginCallCount++
            if (loginDelayMs > 0L) {
                delay(loginDelayMs)
            }
            return loginResult
        }

        override suspend fun register(
            username: String,
            email: String,
            password: String
        ): AppResult<AuthSession> =
            AppResult.Error(AppError.Unknown("Not used in this test"))

        override suspend fun refreshSession(): AppResult<AuthSession> =
            AppResult.Error(AppError.Unknown("Not used in this test"))

        override suspend fun logout() = Unit
    }
}

