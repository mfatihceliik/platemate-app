package com.mefy.platemate.presentation.features.auth.register

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.usecase.auth.CalculateRegisterPasswordStrengthUseCase
import com.mefy.platemate.domain.usecase.auth.RegisterUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.common.messaging.UiMessage
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.register.reducer.RegisterStateReducer
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadingState_preventsSecondSubmitWhileFirstRequestIsRunning() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession()),
            registerDelayMs = 1_000L
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        viewModel.onAction(RegisterUiAction.SubmitClicked)
        viewModel.onAction(RegisterUiAction.SubmitClicked)

        assertTrue(viewModel.uiState.value.isLoading)

        runCurrent()
        assertEquals(1, repository.registerCallCount)

        advanceUntilIdle()
    }

    @Test
    fun errorResult_setsErrorStateAndEmitsSnackbarEffect() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Error(
                AppError.Server(
                    message = "Validation failed",
                    fieldErrors = mapOf("email" to "Email is invalid")
                )
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEvent = async { viewModel.uiMessages.first() }

        viewModel.onAction(RegisterUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val expectedMessage = UiText.Dynamic("Validation failed")
        assertFalse(state.isLoading)
        assertEquals("Email is invalid", state.emailError)
        assertEquals(
            UiMessage.ShowSnackbar(expectedMessage),
            emittedEvent.await()
        )
    }

    @Test
    fun successResult_resetsSubmitStateAndEmitsNavigateEffect() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        val emittedEffect = async { viewModel.uiEffect.first() }

        viewModel.onAction(RegisterUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(RegisterUiEffect.NavigateAfterRegister, emittedEffect.await())
    }

    @Test
    fun invalidEmail_marksLocalValidationAndBlocksSubmit() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(RegisterUiAction.UsernameChanged("fatih"))
        viewModel.onAction(RegisterUiAction.EmailChanged("invalid-email"))
        viewModel.onAction(RegisterUiAction.PasswordChanged("123456"))
        viewModel.onAction(RegisterUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isEmailFormatValid)
        assertFalse(state.isSubmitEnabled)
        assertEquals(0, repository.registerCallCount)
    }

    @Test
    fun passwordChange_updatesStrengthModel() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(RegisterUiAction.PasswordChanged("123456"))
        assertEquals(PasswordStrengthLevel.WEAK, viewModel.uiState.value.passwordStrength.level)

        viewModel.onAction(RegisterUiAction.PasswordChanged("Strong123!"))
        assertEquals(PasswordStrengthLevel.STRONG, viewModel.uiState.value.passwordStrength.level)
        assertEquals(6, viewModel.uiState.value.passwordMinLength)
    }

    @Test
    fun usernameChange_clearsUsernameErrorAndFormMessage() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Error(
                AppError.Server(
                    message = "Validation failed",
                    fieldErrors = mapOf("username" to "Username already exists")
                )
            )
        )
        val viewModel = createViewModel(repository)
        fillValidForm(viewModel)

        viewModel.onAction(RegisterUiAction.SubmitClicked)
        advanceUntilIdle()

        viewModel.onAction(RegisterUiAction.UsernameChanged("new-username"))
        val state = viewModel.uiState.value

        assertNull(state.usernameError)
    }

    @Test
    fun submitAttempt_setsHasSubmittedOnceTrue_whenFormIsInvalid() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(RegisterUiAction.SubmitClicked)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.hasSubmittedOnce)
        assertFalse(state.isSubmitEnabled)
        assertEquals(0, repository.registerCallCount)
    }

    @Test
    fun passwordChange_recomputesStrengthAndSubmitEnabledConsistently() = runTest {
        val repository = FakeAuthRepository(
            registerResult = AppResult.Success(sampleSession())
        )
        val viewModel = createViewModel(repository)

        viewModel.onAction(RegisterUiAction.UsernameChanged("fatih"))
        viewModel.onAction(RegisterUiAction.EmailChanged("fatih@test.com"))
        viewModel.onAction(RegisterUiAction.PasswordChanged("123"))

        assertFalse(viewModel.uiState.value.isPasswordLengthValid)
        assertFalse(viewModel.uiState.value.isSubmitEnabled)

        viewModel.onAction(RegisterUiAction.PasswordChanged("Strong123!"))

        assertTrue(viewModel.uiState.value.isPasswordLengthValid)
        assertTrue(viewModel.uiState.value.isSubmitEnabled)
        assertEquals(PasswordStrengthLevel.STRONG, viewModel.uiState.value.passwordStrength.level)
    }

    private fun createViewModel(repository: FakeAuthRepository): RegisterViewModel =
        RegisterViewModel(
            registerUseCase = RegisterUseCase(repository),
            registerStateReducer = RegisterStateReducer(
                calculateRegisterPasswordStrengthUseCase = CalculateRegisterPasswordStrengthUseCase(),
                validateEmailFormatUseCase = ValidateEmailFormatUseCase()
            ),
            globalUiEventBus = DefaultGlobalUiEventBus()
        )

    private fun fillValidForm(viewModel: RegisterViewModel) {
        viewModel.onAction(RegisterUiAction.UsernameChanged("fatih"))
        viewModel.onAction(RegisterUiAction.EmailChanged("fatih@test.com"))
        viewModel.onAction(RegisterUiAction.PasswordChanged("123456"))
    }

    private fun sampleSession(): AuthSession =
        AuthSession(userId = 1L, username = "fatih", token = "token")

    private class FakeAuthRepository(
        var registerResult: AppResult<AuthSession>,
        var registerDelayMs: Long = 0L
    ) : AuthRepository {

        override val session: Flow<AuthSession?> = MutableStateFlow(null)
        var registerCallCount: Int = 0

        override suspend fun login(email: String, password: String): AppResult<AuthSession> =
            AppResult.Error(AppError.Server("Not used in this test"))

        override suspend fun register(
            username: String,
            email: String,
            password: String
        ): AppResult<AuthSession> {
            registerCallCount++
            if (registerDelayMs > 0L) {
                delay(registerDelayMs)
            }
            return registerResult
        }

        override suspend fun refreshSession(): AppResult<AuthSession> =
            AppResult.Error(AppError.Server("Not used in this test"))

        override suspend fun logout() = Unit
    }
}

