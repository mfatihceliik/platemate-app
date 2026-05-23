package com.mefy.platemate.presentation.features.auth.register.reducer

import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.domain.usecase.auth.CalculateRegisterPasswordStrengthUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.register.RegisterScreenUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterStateReducerTest {

    private val reducer = RegisterStateReducer(
        calculateRegisterPasswordStrengthUseCase = CalculateRegisterPasswordStrengthUseCase(),
        validateEmailFormatUseCase = ValidateEmailFormatUseCase()
    )

    @Test
    fun initialState_usesConfiguredPasswordMinLength() {
        val state = reducer.initialState()

        assertEquals(CalculateRegisterPasswordStrengthUseCase().minPasswordLength, state.passwordMinLength)
    }

    @Test
    fun onPasswordChanged_recomputesStrengthAndSubmitEligibility() {
        val base = RegisterScreenUiState(
            username = "fatih",
            email = "fatih@test.com"
        )

        val weak = reducer.onPasswordChanged(base, "123")
        val strong = reducer.onPasswordChanged(base, "Strong123!")

        assertFalse(weak.isPasswordLengthValid)
        assertFalse(weak.isSubmitEnabled)
        assertTrue(strong.isPasswordLengthValid)
        assertTrue(strong.isSubmitEnabled)
        assertEquals(PasswordStrengthLevel.STRONG, strong.passwordStrength.level)
    }

    @Test
    fun onSubmitError_setsFieldErrorsAndErrorState() {
        val state = reducer.onSubmitError(
            state = RegisterScreenUiState(),
            message = UiText.Dynamic("Validation failed"),
            fieldErrors = mapOf(
                "username" to UiText.Dynamic("Username already exists"),
                "email" to UiText.Dynamic("Email invalid"),
                "password" to UiText.Dynamic("Password too short")
            )
        )

        assertTrue(state.submitState is UiActionState.Error)
        assertEquals("Username already exists", state.usernameError)
        assertEquals("Email invalid", state.emailError)
        assertEquals("Password too short", state.passwordError)
        assertEquals(UiText.Dynamic("Validation failed"), state.formMessage)
    }
}
