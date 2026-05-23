package com.mefy.platemate.core.error

sealed class AppError(
    open val message: String? = null,
    open val code: Int? = null,
    open val cause: Throwable? = null,
    open val rawBody: String? = null,
    open val fieldErrors: Map<String, String>? = null,
    open val backendMessage: String? = null
) {
    data class Backend(
        override val message: String?,
        override val code: Int? = null,
        override val cause: Throwable? = null,
        override val rawBody: String? = null,
        override val fieldErrors: Map<String, String>? = null,
        override val backendMessage: String? = message
    ) : AppError(message, code, cause, rawBody, fieldErrors, backendMessage)

    data class Http(
        override val code: Int,
        override val message: String?,
        override val cause: Throwable? = null,
        override val rawBody: String? = null,
        override val fieldErrors: Map<String, String>? = null,
        override val backendMessage: String? = null
    ) : AppError(message, code, cause, rawBody, fieldErrors, backendMessage)

    data class Network(
        override val message: String?,
        override val cause: Throwable? = null
    ) : AppError(message, cause = cause)

    data class ServerUnavailable(
        override val message: String?,
        override val cause: Throwable? = null
    ) : AppError(message, cause = cause)

    data class Unknown(
        override val message: String?,
        override val cause: Throwable? = null
    ) : AppError(message, cause = cause)

    data class Serialization(
        override val message: String?,
        override val cause: Throwable? = null
    ) : AppError(message, cause = cause)

    object Unauthorized : AppError(
        message = "Unauthorized",
        code = 401,
        backendMessage = "Unauthorized"
    )

    object EmptyData : AppError(message = "Response data is empty")
}

