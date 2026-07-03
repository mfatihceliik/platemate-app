package com.mefy.platemate.data.remote

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import kotlinx.coroutines.CancellationException

suspend fun <T> safeApiCall(call: suspend () -> DataResultResponse<T>): AppResult<T> =
    wrapApiCall {
        val response = call()
        when {
            !response.success -> AppResult.Error(AppError.Api(message = response.message))
            response.data == null -> AppResult.Error(AppError.Network())
            else -> AppResult.Success(response.data)
        }
    }

suspend fun safeResultCall(call: suspend () -> ResultResponse): AppResult<Unit> =
    wrapApiCall {
        val response = call()
        if (response.success) AppResult.Success(Unit)
        else AppResult.Error(AppError.Api(message = response.message))
    }

private suspend inline fun <T> wrapApiCall(
    crossinline block: suspend () -> AppResult<T>
): AppResult<T> = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    AppResult.Error(ApiErrorClassifier.classify(e))
}
