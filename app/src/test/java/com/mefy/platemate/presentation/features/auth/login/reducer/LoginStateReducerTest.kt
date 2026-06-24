package com.mefy.platemate.presentation.features.auth.login.reducer

import com.mefy.platemate.domain.usecase.auth.ValidateEmailFormatUseCase
import com.mefy.platemate.domain.usecase.auth.ValidateLoginFormUseCase
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
    fun onEmailChanged_recomputesValidationAndClearsErrors() {
        val startState = LoginScreenUiState(
            emailError = "invalid"
        )

        val state = reducer.onEmailChanged(startState, "invalid-email")

        assertEquals("invalid-email", state.email)
        assertNull(state.emailError)
        assertFalse(state.isEmailFormatValid)
    }

    @Test
    fun onSubmitError_setsFieldErrorsAndIdleState() {
        val state = reducer.onSubmitError(
            state = LoginScreenUiState(email = "bad", password = "123456"),
            fieldErrors = mapOf(
                "email" to "Email invalid",
                "password" to "Password invalid"
            )
        )

        assertFalse(state.isLoading)
        assertEquals("Email invalid", state.emailError)
        assertEquals("Password invalid", state.passwordError)
    }
}
