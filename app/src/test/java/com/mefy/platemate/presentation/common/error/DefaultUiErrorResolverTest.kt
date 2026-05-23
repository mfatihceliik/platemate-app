package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultUiErrorResolverTest {

    private val resolver = DefaultUiErrorResolver()

    @Test
    fun loginUnauthorized_mapsToInvalidCredentialsMessage() {
        val result = resolver.resolve(AppError.Unauthorized, ErrorContext.Login)

        assertEquals(UiText.Resource(R.string.auth_login_invalid_credentials), result.message)
        assertEquals(UiErrorUxAction.SHOW_SNACKBAR, result.uxAction)
    }

    @Test
    fun genericUnauthorized_mapsToCommonUnauthorizedMessage() {
        val result = resolver.resolve(AppError.Unauthorized, ErrorContext.Generic)

        assertEquals(UiText.Resource(R.string.common_error_unauthorized), result.message)
        assertEquals(UiErrorUxAction.SHOW_SNACKBAR, result.uxAction)
    }

    @Test
    fun backendFieldErrors_areMappedAsUiTextDynamic() {
        val error = AppError.Backend(
            message = "Validation failed",
            fieldErrors = mapOf(
                "email" to "Email is invalid",
                "password" to "Password is too short"
            )
        )

        val result = resolver.resolve(error, ErrorContext.Register)

        assertEquals(UiText.Dynamic("Email is invalid"), result.fieldErrors["email"])
        assertEquals(UiText.Dynamic("Password is too short"), result.fieldErrors["password"])
        assertEquals(UiErrorUxAction.SHOW_SNACKBAR, result.uxAction)
    }

    @Test
    fun loginFieldErrors_dropKeysOutsideWhitelist() {
        val error = AppError.Backend(
            message = "Validation failed",
            fieldErrors = mapOf(
                "email" to "Email is invalid",
                "password" to "Password is too short",
                "internalDebug" to "Should not be exposed"
            )
        )

        val result = resolver.resolve(error, ErrorContext.Login)

        assertEquals(UiText.Dynamic("Email is invalid"), result.fieldErrors["email"])
        assertEquals(UiText.Dynamic("Password is too short"), result.fieldErrors["password"])
        assertFalse(result.fieldErrors.containsKey("internalDebug"))
    }

    @Test
    fun searchContext_dropsAllFieldErrors() {
        val error = AppError.Backend(
            message = "Validation failed",
            fieldErrors = mapOf("email" to "Email is invalid")
        )

        val result = resolver.resolve(error, ErrorContext.Search)

        assertTrue(result.fieldErrors.isEmpty())
    }

    @Test
    fun sessionGateUnauthorized_requestsNavigateAuthAction() {
        val result = resolver.resolve(AppError.Unauthorized, ErrorContext.SessionGate)

        assertEquals(UiErrorUxAction.NAVIGATE_AUTH, result.uxAction)
    }

    @Test
    fun sessionGateForbidden_requestsNavigateAuthAction() {
        val result = resolver.resolve(
            AppError.Http(code = 403, message = "forbidden"),
            ErrorContext.SessionGate
        )

        assertEquals(UiErrorUxAction.NAVIGATE_AUTH, result.uxAction)
    }

    @Test
    fun searchContext_disablesGlobalUxAction() {
        val result = resolver.resolve(AppError.Network("offline"), ErrorContext.Search)

        assertEquals(UiErrorUxAction.NONE, result.uxAction)
        assertEquals(UiText.Resource(R.string.common_error_network), result.message)
    }

    @Test
    fun genericHttpError_withoutMessage_mapsToHttpFallback() {
        val result = resolver.resolve(
            AppError.Http(code = 500, message = null),
            ErrorContext.Generic
        )

        assertEquals(UiText.Resource(R.string.common_error_http), result.message)
    }

    @Test
    fun genericUnknownError_withoutMessage_mapsToUnknownFallback() {
        val result = resolver.resolve(AppError.Unknown(message = null), ErrorContext.Generic)

        assertTrue(result.message is UiText.Resource)
        assertEquals(UiText.Resource(R.string.common_error_unknown), result.message)
    }

    @Test
    fun unknownError_withMessage_stillMapsToUnknownFallbackResource() {
        val result = resolver.resolve(
            AppError.Unknown(message = "Expected BEGIN_OBJECT ..."),
            ErrorContext.Generic
        )

        assertEquals(UiText.Resource(R.string.common_error_unknown), result.message)
    }

    @Test
    fun httpError_withMessage_stillMapsToResourceFallback() {
        val result = resolver.resolve(
            AppError.Http(code = 500, message = "Internal Server Error"),
            ErrorContext.Generic
        )

        assertEquals(UiText.Resource(R.string.common_error_http), result.message)
    }

    @Test
    fun serializationError_mapsToUnknownFallbackResource() {
        val result = resolver.resolve(
            AppError.Serialization(message = "Expected BEGIN_OBJECT ..."),
            ErrorContext.Generic
        )

        assertEquals(UiText.Resource(R.string.common_error_unknown), result.message)
        assertNull(result.fieldErrors["internalDebug"])
    }
}

