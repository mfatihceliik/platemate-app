package com.mefy.platemate.data.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import java.io.IOException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

private val gson = Gson()

suspend fun <T> safeApiCall(call: suspend () -> DataResultResponse<T>): AppResult<T> {
    return try {
        val response = call()
        when {
            !response.success -> AppResult.Error(AppError.Server(message = response.message))
            // Başarılı zarf ama veri yok: beklenmeyen/bozuk yanıt -> sunucuya ulaşılamadı say.
            response.data == null -> AppResult.Error(AppError.Unreachable())
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
            AppResult.Error(AppError.Server(message = response.message))
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
    this is HttpException -> toHttpAppError()
    // DNS çözülemiyorsa cihaz büyük olasılıkla çevrimdışı -> ağ hatası.
    this is UnknownHostException -> AppError.Network(cause = this)
    // Diğer I/O hataları (zaman aşımı, bağlantı reddi, callTimeout) -> sunucuya ulaşılamadı.
    this is IOException -> AppError.Unreachable(cause = this)
    else -> AppError.Unreachable(cause = this)
}

private fun HttpException.toHttpAppError(): AppError {
    val statusCode = code()
    return when {
        statusCode == 401 || statusCode == 403 -> AppError.SessionExpired
        statusCode >= 500 -> AppError.Unreachable(cause = this)
        else -> {
            val rawBody = response()?.errorBody()?.string()?.takeIf { it.isNotBlank() }
            val envelope = rawBody?.let(::parseBackendErrorEnvelope)
            val backendMessage = envelope?.message?.takeIf { it.isNotBlank() }
            AppError.Server(
                message = backendMessage ?: message(),
                fieldErrors = envelope?.data.toFieldErrors(),
                cause = this
            )
        }
    }
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
