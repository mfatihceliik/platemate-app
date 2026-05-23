package com.mefy.platemate.presentation.features.auth.login.reducer

import com.mefy.platemate.R
import com.mefy.platemate.domain.model.auth.EmailValidationReason
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.login.LoginScreenUiState
import javax.inject.Inject

class LoginStateReducer @Inject constructor(
    private val validateLoginFormUseCase: ValidateLoginFormUseCase
) {

    fun onEmailChanged(
        state: LoginScreenUiState,
        value: String
    ): LoginScreenUiState = recomputeDerivedState(
        state.copy(email = value, emailError = null, formMessage = null)
    )

    fun onPasswordChanged(
        state: LoginScreenUiState,
        value: String
    ): LoginScreenUiState = recomputeDerivedState(
        state.copy(password = value, passwordError = null, formMessage = null)
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

    fun onInvalidSubmit(state: LoginScreenUiState): LoginScreenUiState = state.copy(
        submitState = UiActionState.Idle,
        formMessage = UiText.Resource(R.string.auth_login_form_invalid)
    )

    fun onSubmitLoading(state: LoginScreenUiState): LoginScreenUiState =
        state.copy(submitState = UiActionState.Loading, formMessage = null)

    fun onSubmitSuccess(state: LoginScreenUiState): LoginScreenUiState =
        state.copy(submitState = UiActionState.Idle, formMessage = null)

    fun onSubmitError(
        state: LoginScreenUiState,
        message: UiText,
        fieldErrors: Map<String, UiText>
    ): LoginScreenUiState {
        val stateWithFieldErrors = state.copy(
            emailError = fieldErrors.dynamicValueOrNull("email", "identifier", "username"),
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
