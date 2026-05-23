package com.mefy.platemate.presentation.features.auth.login.reducer

import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
import com.mefy.platemate.presentation.common.state.UiActionState
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.login.LoginScreenUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginStateReducerTest {

    private val reducer = LoginStateReducer(
        validateLoginFormUseCase = ValidateLoginFormUseCase(ValidateEmailFormatUseCase())
    )

    @Test
    fun onEmailChanged_recomputesValidationAndClearsMessages() {
        val startState = LoginScreenUiState(
            formMessage = UiText.Dynamic("error"),
            emailError = "invalid"
        )

        val state = reducer.onEmailChanged(startState, "invalid-email")

        assertEquals("invalid-email", state.email)
        assertNull(state.emailError)
        assertNull(state.formMessage)
        assertFalse(state.isEmailFormatValid)
    }

    @Test
    fun onSubmitError_setsFieldErrorsAndErrorState() {
        val state = reducer.onSubmitError(
            state = LoginScreenUiState(email = "bad", password = "123456"),
            message = UiText.Dynamic("Login failed"),
            fieldErrors = mapOf(
                "email" to UiText.Dynamic("Email invalid"),
                "password" to UiText.Dynamic("Password invalid")
            )
        )

        assertTrue(state.submitState is UiActionState.Error)
        assertEquals("Email invalid", state.emailError)
        assertEquals("Password invalid", state.passwordError)
        assertEquals(UiText.Dynamic("Login failed"), state.formMessage)
    }
}
