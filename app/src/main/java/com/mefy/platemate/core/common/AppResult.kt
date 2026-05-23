package com.mefy.platemate.core.common

import com.mefy.platemate.core.error.AppError

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}

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

inline fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (AppError) -> R
): R = when (this) {
    is AppResult.Success -> onSuccess(data)
    is AppResult.Error -> onError(error)
}

inline fun <T> AppResult<T>.mapError(transform: (AppError) -> AppError): AppResult<T> = when (this) {
    is AppResult.Success -> this
    is AppResult.Error -> AppResult.Error(transform(error))
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

inline fun <T> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) {
        action(error)
    }
    return this
}

fun <T : Any> T?.toResultOr(error: AppError): AppResult<T> =
    this?.let { value -> AppResult.Success(value) } ?: AppResult.Error(error)
