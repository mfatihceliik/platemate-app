package com.mefy.platemate.core.common.result

import com.mefy.platemate.core.error.AppError

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}