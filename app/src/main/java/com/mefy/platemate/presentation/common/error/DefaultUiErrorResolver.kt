package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.text.UiText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUiErrorResolver @Inject constructor() : UiErrorResolver {

    override fun resolve(error: AppError, context: ErrorContext): ResolvedUiError {
        val message = when {
            context is ErrorContext.Login && error.isUnauthorized() -> {
                UiText.Resource(R.string.auth_login_invalid_credentials)
            }

            else -> mapDefaultMessage(error)
        }

        val fieldErrors = resolveWhitelistedFieldErrors(error, context)

        val uxAction = when {
            context is ErrorContext.SessionGate && error.requiresReAuthentication() -> UiErrorUxAction.NAVIGATE_AUTH
            context is ErrorContext.Search -> UiErrorUxAction.NONE
            context is ErrorContext.SessionGate -> UiErrorUxAction.NONE
            else -> UiErrorUxAction.SHOW_SNACKBAR
        }

        return ResolvedUiError(
            message = message,
            fieldErrors = fieldErrors,
            uxAction = uxAction
        )
    }

    private fun mapDefaultMessage(error: AppError): UiText = when (error) {
        AppError.Unauthorized -> UiText.Resource(R.string.common_error_unauthorized)
        AppError.EmptyData -> UiText.Resource(R.string.common_error_empty_data)
        is AppError.ServerUnavailable -> UiText.Resource(R.string.common_error_server_unavailable)
        is AppError.Network -> UiText.Resource(R.string.common_error_network)
        is AppError.Backend -> UiText.Resource(R.string.common_error_unknown)

        is AppError.Http -> {
            if (error.code == 401) {
                UiText.Resource(R.string.common_error_unauthorized)
            } else {
                UiText.Resource(R.string.common_error_http)
            }
        }

        is AppError.Unknown -> UiText.Resource(R.string.common_error_unknown)
        is AppError.Serialization -> UiText.Resource(R.string.common_error_unknown)
    }

    private fun AppError.isUnauthorized(): Boolean = when (this) {
        AppError.Unauthorized -> true
        is AppError.Http -> code == 401
        else -> false
    }

    private fun AppError.requiresReAuthentication(): Boolean = when (this) {
        AppError.Unauthorized -> true
        is AppError.Http -> code == 401 || code == 403
        else -> false
    }

    private fun resolveWhitelistedFieldErrors(
        error: AppError,
        context: ErrorContext
    ): Map<String, UiText> {
        val allowedKeys = allowedFieldKeys(context)
        if (allowedKeys.isEmpty()) {
            return emptyMap()
        }

        return error.fieldErrors.orEmpty()
            .asSequence()
            .mapNotNull { (key, value) ->
                val normalizedKey = key.trim().lowercase()
                if (normalizedKey !in allowedKeys || value.isBlank()) {
                    null
                } else {
                    normalizedKey to UiText.Dynamic(value)
                }
            }
            .toMap()
    }

    private fun allowedFieldKeys(context: ErrorContext): Set<String> = when (context) {
        is ErrorContext.Login -> setOf("identifier", "username", "email", "password")
        is ErrorContext.Register -> setOf("username", "email", "password")
        is ErrorContext.Generic,
        is ErrorContext.Profile,
        is ErrorContext.Search,
        is ErrorContext.SessionGate -> emptySet()
    }
}

