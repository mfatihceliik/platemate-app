package com.mefy.platemate.data.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mefy.platemate.core.error.AppError
import java.io.IOException
import java.net.UnknownHostException
import retrofit2.HttpException

internal object ApiErrorClassifier {

    private val gson = Gson()

    fun classify(throwable: Throwable): AppError = when (throwable) {
        is HttpException -> classifyHttp(throwable)
        is UnknownHostException -> AppError.Network(isOffline = true, cause = throwable)
        is IOException -> AppError.Network(cause = throwable)
        else -> AppError.Network(cause = throwable)
    }

    private fun classifyHttp(e: HttpException): AppError = when {
        e.code() == 401 -> AppError.SessionExpired
        e.code() >= 500 -> AppError.Network(cause = e)
        else -> parseApiError(e)
    }

    private fun parseApiError(e: HttpException): AppError.Api {
        val rawBody = e.response()?.errorBody()?.string()?.takeIf { it.isNotBlank() }
        val envelope = rawBody?.let(::parseBackendErrorEnvelope)
        val backendMessage = envelope?.message?.takeIf { it.isNotBlank() }
        return AppError.Api(
            message = backendMessage ?: e.message(),
            errorCode = envelope?.data.extractErrorCode(),
            fieldErrors = envelope?.data.toFieldErrors()
        )
    }

    private data class BackendErrorEnvelope(
        val success: Boolean? = null,
        val message: String? = null,
        val data: JsonElement? = null
    )

    private fun parseBackendErrorEnvelope(rawBody: String): BackendErrorEnvelope? =
        runCatching { gson.fromJson(rawBody, BackendErrorEnvelope::class.java) }.getOrNull()

    private fun JsonElement?.extractErrorCode(): String? {
        val obj = this as? JsonObject ?: return null
        val codeElement = obj.get("code") ?: return null
        return if (codeElement.isJsonPrimitive) codeElement.asString else null
    }

    private fun JsonElement?.toFieldErrors(): Map<String, String>? {
        val objectData = this as? JsonObject ?: return null
        val fieldErrors = objectData.entrySet().mapNotNull { (key, value) ->
            if (key == "code") return@mapNotNull null
            if (value.isJsonNull) return@mapNotNull null
            val mappedValue = if (value.isJsonPrimitive) {
                value.asJsonPrimitive.asString
            } else {
                value.toString()
            }
            key to mappedValue
        }.toMap()
        return fieldErrors.takeIf { it.isNotEmpty() }
    }
}
