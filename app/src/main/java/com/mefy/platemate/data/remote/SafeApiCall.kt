package com.mefy.platemate.data.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import java.io.IOException
import java.net.ConnectException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

private val gson = Gson()

suspend fun <T> safeApiCall(call: suspend () -> DataResultResponse<T>): AppResult<T> {
    return try {
        val response = call()
        when {
            !response.success -> AppResult.Error(AppError.Backend(response.message))
            response.data == null -> AppResult.Error(AppError.EmptyData)
            else -> AppResult.Success(response.data)
        }
    } catch (throwable: Throwable) {
        throwable.toAppResultError()
    }
}

suspend fun safeMessageCall(call: suspend () -> ResultResponse): AppResult<Unit> {
    return try {
        val response = call()
        if (response.success) {
            AppResult.Success(Unit)
        } else {
            AppResult.Error(AppError.Backend(response.message))
        }
    } catch (throwable: Throwable) {
        throwable.toAppResultError()
    }
}

private fun Throwable.toAppResultError(): AppResult.Error {
    if (this is CancellationException) {
        throw this
    }
    return AppResult.Error(toAppError())
}

private fun Throwable.toAppError(): AppError = when {
    isSerializationFailure() -> AppError.Serialization(message = message, cause = this)
    this is HttpException -> toHttpAppError()
    this is ConnectException -> AppError.ServerUnavailable(message = message, cause = this)
    this is IOException -> AppError.Network(message = message, cause = this)
    else -> AppError.Unknown(message = message, cause = this)
}

private fun Throwable.isSerializationFailure(): Boolean {
    if (this is JsonParseException || this is JsonSyntaxException) {
        return true
    }

    if (this is IllegalStateException && message.isLikelyGsonStructureMismatch()) {
        return true
    }

    return generateSequence(cause) { it.cause }.any { rootCause ->
        rootCause is JsonParseException ||
            rootCause is JsonSyntaxException ||
            (rootCause is IllegalStateException && rootCause.message.isLikelyGsonStructureMismatch())
    }
}

private fun String?.isLikelyGsonStructureMismatch(): Boolean {
    if (this.isNullOrBlank()) {
        return false
    }
    return contains("Expected BEGIN_") || (contains(" at line ") && contains(" path \$"))
}

private fun HttpException.toHttpAppError(): AppError {
    val statusCode = code()
    if (statusCode == 401) {
        return AppError.Unauthorized
    }

    val rawBody = response()?.errorBody()?.string()?.takeIf { it.isNotBlank() }
    val parsedErrorEnvelope = rawBody?.let(::parseBackendErrorEnvelope)
    val backendMessage = parsedErrorEnvelope?.message?.takeIf { it.isNotBlank() }

    return AppError.Http(
        code = statusCode,
        message = backendMessage ?: message(),
        cause = this,
        rawBody = rawBody,
        fieldErrors = parsedErrorEnvelope?.data.toFieldErrors(),
        backendMessage = backendMessage
    )
}

private data class BackendErrorEnvelope(
    val success: Boolean? = null,
    val message: String? = null,
    val data: JsonElement? = null
)

private fun parseBackendErrorEnvelope(rawBody: String): BackendErrorEnvelope? =
    runCatching { gson.fromJson(rawBody, BackendErrorEnvelope::class.java) }.getOrNull()

private fun JsonElement?.toFieldErrors(): Map<String, String>? {
    val objectData = this as? JsonObject ?: return null
    val fieldErrors = objectData.entrySet().mapNotNull { (key, value) ->
        if (value.isJsonNull) {
            return@mapNotNull null
        }
        val mappedValue = if (value.isJsonPrimitive) {
            value.asJsonPrimitive.asString
        } else {
            value.toString()
        }
        key to mappedValue
    }.toMap()
    return fieldErrors.takeIf { it.isNotEmpty() }
}

