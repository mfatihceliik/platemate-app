package com.mefy.platemate.presentation.features.auth.register.reducer

import com.mefy.platemate.R
import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.usecase.auth.CalculateRegisterPasswordStrengthUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
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
        state.copy(username = value, usernameError = null, formMessage = null)
    )

    fun onEmailChanged(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState = recomputeDerivedState(
        state.copy(email = value, emailError = null, formMessage = null)
    )

    fun onPasswordChanged(
        state: RegisterScreenUiState,
        value: String
    ): RegisterScreenUiState = recomputeDerivedState(
        state.copy(password = value, passwordError = null, formMessage = null)
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

    fun onInvalidSubmit(state: RegisterScreenUiState): RegisterScreenUiState = state.copy(
        submitState = UiActionState.Idle,
        formMessage = UiText.Resource(R.string.auth_register_form_invalid)
    )

    fun onSubmitLoading(state: RegisterScreenUiState): RegisterScreenUiState =
        state.copy(submitState = UiActionState.Loading, formMessage = null)

    fun onSubmitSuccess(state: RegisterScreenUiState): RegisterScreenUiState =
        state.copy(submitState = UiActionState.Idle, formMessage = null)

    fun onSubmitError(
        state: RegisterScreenUiState,
        message: UiText,
        fieldErrors: Map<String, UiText>
    ): RegisterScreenUiState {
        val stateWithFieldErrors = state.copy(
            usernameError = fieldErrors.dynamicValueOrNull("username"),
            emailError = fieldErrors.dynamicValueOrNull("email"),
            passwordError = fieldErrors.dynamicValueOrNull("password")
        )

        return recomputeDerivedState(
            stateWithFieldErrors.copy(
                submitState = UiActionState.Error,
                formMessage = message
            )
        )
    }

    private fun Map<String, UiText>.dynamicValueOrNull(vararg keys: String): String? =
        keys.asSequence()
            .mapNotNull { key -> this[key].asDynamicValueOrNull() }
            .firstOrNull()

    private fun UiText?.asDynamicValueOrNull(): String? = when (this) {
        is UiText.Dynamic -> value
        else -> null
    }

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
