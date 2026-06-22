package com.mefy.platemate.presentation.features.auth.login.reducer

import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
import com.mefy.platemate.presentation.features.auth.login.LoginScreenUiState
import javax.inject.Inject

class LoginStateReducer @Inject constructor(
    private val validateLoginFormUseCase: ValidateLoginFormUseCase
) {

    fun onEmailChanged(
        state: LoginScreenUiState,
        value: String
    ): LoginScreenUiState = recomputeDerivedState(
        state.copy(email = value, emailError = null)
    )

    fun onPasswordChanged(
        state: LoginScreenUiState,
        value: String
    ): LoginScreenUiState = recomputeDerivedState(
        state.copy(password = value, passwordError = null)
    )

    fun onPrefillEmailReceived(
        state: LoginScreenUiState,
        value: String
    ): LoginScreenUiState {
        if (value.isBlank()) return state
        return recomputeDerivedState(
            state.copy(email = value, emailError = null)
        )
    }

    fun prepareSubmitAttempt(state: LoginScreenUiState): LoginScreenUiState =
        recomputeDerivedState(state.copy(hasSubmittedOnce = true))

    fun onInvalidSubmit(state: LoginScreenUiState): LoginScreenUiState =
        state.copy(isLoading = false)

    fun onSubmitLoading(state: LoginScreenUiState): LoginScreenUiState =
        state.copy(isLoading = true)

    fun onSubmitSuccess(state: LoginScreenUiState): LoginScreenUiState =
        state.copy(isLoading = false)

    fun onSubmitError(
        state: LoginScreenUiState,
        fieldErrors: Map<String, String>
    ): LoginScreenUiState {
        val stateWithFieldErrors = state.copy(
            emailError = fieldErrors.firstValue("email", "identifier", "username"),
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

    private fun recomputeDerivedState(state: LoginScreenUiState): LoginScreenUiState {
        val validationResult = validateLoginFormUseCase(
            email = state.email,
            password = state.password
        )

        return state.copy(
            isEmailFormatValid = validationResult.emailReason != EmailValidationReason.INVALID_FORMAT,
            isSubmitEnabled = validationResult.isSubmitEnabled
        )
    }
}
