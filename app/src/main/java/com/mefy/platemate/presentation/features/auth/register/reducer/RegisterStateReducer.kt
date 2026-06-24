package com.mefy.platemate.presentation.features.auth.register.reducer

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.usecase.auth.CalculateRegisterPasswordStrengthUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.presentation.features.auth.register.PasswordStrength
import com.mefy.platemate.presentation.features.auth.register.RegisterScreenUiState
import javax.inject.Inject

class RegisterStateReducer @Inject constructor(
    private val calculateRegisterPasswordStrengthUseCase: CalculateRegisterPasswordStrengthUseCase,
    private val validateEmailFormatUseCase: ValidateEmailFormatUseCase
) {

    fun initialState(): RegisterScreenUiState = RegisterScreenUiState(
        passwordMinLength = calculateRegisterPasswordStrengthUseCase.minPasswordLength
    )

    fun onUsernameChanged(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState = recomputeDerivedState(
        state.copy(username = value, usernameError = null)
    )

    fun onEmailChanged(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState = recomputeDerivedState(
        state.copy(email = value, emailError = null)
    )

    fun onPasswordChanged(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState = recomputeDerivedState(
        state.copy(password = value, passwordError = null)
    )

    fun onPrefillIdentifierReceived(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState {
        if (value.isBlank()) return state
        val updatedState = if (value.contains("@")) {
            state.copy(email = value, emailError = null)
        } else {
            state.copy(username = value, usernameError = null)
        }
        return recomputeDerivedState(updatedState)
    }

    fun prepareSubmitAttempt(state: RegisterScreenUiState): RegisterScreenUiState =
        recomputeDerivedState(state.copy(hasSubmittedOnce = true))

    fun onInvalidSubmit(state: RegisterScreenUiState): RegisterScreenUiState =
        state.copy(isLoading = false)

    fun onSubmitLoading(state: RegisterScreenUiState): RegisterScreenUiState =
        state.copy(isLoading = true)

    fun onSubmitSuccess(state: RegisterScreenUiState): RegisterScreenUiState =
        state.copy(isLoading = false)

    fun onSubmitError(
        state: RegisterScreenUiState,
        fieldErrors: Map<String, String>
    ): RegisterScreenUiState {
        val stateWithFieldErrors = state.copy(
            usernameError = fieldErrors.firstValue("username"),
            emailError = fieldErrors.firstValue("email"),
            passwordError = fieldErrors.firstValue("password")
        )

        return recomputeDerivedState(
            stateWithFieldErrors.copy(isLoading = false)
        )
    }

    private fun Map<String, String>.firstValue(vararg keys: String): String? =
        keys.asSequence()
            .mapNotNull { key -> this[key]?.takeIf { it.isNotBlank() } }
            .firstOrNull()

    private fun recomputeDerivedState(state: RegisterScreenUiState): RegisterScreenUiState {
        val strengthResult = calculateRegisterPasswordStrengthUseCase(state.password)
        val emailValidationReason = validateEmailFormatUseCase(state.email)
        val localEmailValid = emailValidationReason != EmailValidationReason.INVALID_FORMAT
        val localPasswordValid = strengthResult.meetsMinLength
        val submitEnabled = state.username.isNotBlank() &&
            state.email.isNotBlank() &&
            state.password.isNotBlank() &&
            localEmailValid &&
            localPasswordValid

        return state.copy(
            isEmailFormatValid = localEmailValid,
            isPasswordLengthValid = localPasswordValid,
            passwordMinLength = strengthResult.minLength,
            passwordStrength = PasswordStrength(
                level = strengthResult.level,
                progress = strengthResult.progress
            ),
            isSubmitEnabled = submitEnabled
        )
    }
}
