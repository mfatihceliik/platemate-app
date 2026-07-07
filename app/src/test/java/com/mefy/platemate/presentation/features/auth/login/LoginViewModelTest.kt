package com.mefy.platemate.presentation.features.auth.login

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.usecase.auth.LoginUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.messaging.UiMessage
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

        assertTrue(viewModel.uiState.value.isLoading)

        runCurrent()
        assertEquals(1, repository.loginCallCount)

        advanceUntilIdle()
    }

    @Test
    fun errorResult_setsErrorState_mapsEmailAndPasswordErrors_andEmitsSnackbar() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.Api(
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

        val emittedEvent = async { viewModel.uiMessages.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val expectedMessage = UiText.Dynamic("Login failed")
        assertFalse(state.isLoading)
        assertEquals("Email format is invalid", state.emailError)
        assertEquals("Password is incorrect", state.passwordError)
        assertEquals(
            UiMessage.ShowSnackbar(expectedMessage),
            emittedEvent.await()
        )
    }

    @Test
    fun networkError_emitsErrorBannerAndResetsLoading() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.Network()
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.uiMessages.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        // Ağ hatası artık global dialog değil, ekran-yerel kırmızı banner ile gösterilir.
        assertTrue(emittedEvent.await() is UiMessage.ShowSnackbar)
    }

    @Test
    fun unauthorizedError_showsInvalidCredentialsMessage_andEmitsSnackbar() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(AppError.SessionExpired)
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.uiMessages.first() }

        viewModel.onAction(LoginUiAction.SubmitClicked)
        advanceUntilIdle()

        val expectedMessage = UiText.Resource(R.string.auth_login_invalid_credentials)
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(UiMessage.ShowSnackbar(expectedMessage), emittedEvent.await())
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
        assertFalse(state.isLoading)
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
        assertEquals(0, repository.loginCallCount)
    }

    @Test
    fun emailChange_clearsEmailErrorAndFormMessage() = runTest {
        val repository = FakeAuthRepository(
            loginResult = AppResult.Error(
                AppError.Api(
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
        assertEquals(0, repository.loginCallCount)
    }

    private fun createViewModel(
        repository: FakeAuthRepository,
        globalUiEventBus: DefaultGlobalUiEventBus = DefaultGlobalUiEventBus()
    ): LoginViewModel =
        LoginViewModel(
            loginUseCase = LoginUseCase(repository),
            loginStateReducer = LoginStateReducer(
                validateLoginFormUseCase = ValidateLoginFormUseCase(ValidateEmailFormatUseCase())
            ),
            globalUiEventBus = globalUiEventBus
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
            AppResult.Error(AppError.Api("Not used in this test"))

        override suspend fun refreshSession(): AppResult<AuthSession> =
            AppResult.Error(AppError.Api("Not used in this test"))

        override suspend fun changePassword(currentPassword: String, newPassword: String): AppResult<Unit> =
            AppResult.Error(AppError.Api("Not used in this test"))

        override suspend fun logout() = Unit
    }
}

