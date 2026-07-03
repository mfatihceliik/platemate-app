package com.mefy.platemate.core.common.result

import com.mefy.platemate.core.error.AppError


inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

inline fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> = when (this) {
    is AppResult.Success -> transform(data)
    is AppResult.Error -> this
}

suspend inline fun <T, R> AppResult<T>.flatMapSuspend(
    crossinline transform: suspend (T) -> AppResult<R>
): AppResult<R> = when (this) {
    is AppResult.Success -> transform(data)
    is AppResult.Error -> this
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) {
        action(data)
    }
    return this
}

suspend inline fun <T> AppResult<T>.onSuccessSuspend(
    crossinline action: suspend (T) -> Unit
): AppResult<T> {
    if (this is AppResult.Success) {
        action(data)
    }
    return this
}

fun <T : Any> T?.toResultOr(error: AppError): AppResult<T> =
    this?.let { value -> AppResult.Success(value) } ?: AppResult.Error(error)