package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.core.error.AppError

interface UiErrorResolver {
    fun resolve(error: AppError, context: ErrorContext): ResolvedUiError
}
