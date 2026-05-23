package com.mefy.platemate.presentation.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.error.DefaultUiErrorResolver
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.ResolvedUiError
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.error.UiErrorUxAction
import com.mefy.platemate.presentation.common.event.CommonDialogModel
import com.mefy.platemate.presentation.common.event.CommonUiEvent
import com.mefy.platemate.presentation.common.text.UiText
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val uiErrorResolver: UiErrorResolver = DefaultUiErrorResolver()
) : ViewModel() {

    private val _commonUiEvents = MutableSharedFlow<CommonUiEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val commonUiEvents: SharedFlow<CommonUiEvent> = _commonUiEvents.asSharedFlow()

    protected open fun handleError(error: Throwable) {
        if (error is CancellationException) return
        Log.e(TAG, "Error: ${error.message ?: "Unknown error"}", error)
    }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        onError: (Throwable) -> Unit = { handleError(it) },
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(context) {
            try {
                block()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Throwable) {
                onError(exception)
            }
        }
    }

    protected fun showSnackbar(message: UiText) {
        emitCommonUiEvent(CommonUiEvent.ShowSnackbar(message))
    }

    protected fun showDialog(dialog: CommonDialogModel) {
        emitCommonUiEvent(CommonUiEvent.ShowDialog(dialog))
    }

    protected fun resolveUiError(
        error: AppError,
        context: ErrorContext = ErrorContext.Generic,
        consumeUxAction: Boolean = true
    ): ResolvedUiError {
        val resolvedError = uiErrorResolver.resolve(error, context)
        if (consumeUxAction) {
            consumeResolvedUxAction(resolvedError)
        }
        return resolvedError
    }

    protected fun handleError(
        error: AppError,
        context: ErrorContext = ErrorContext.Generic,
        consumeUxAction: Boolean = true,
        applyFieldErrors: (Map<String, UiText>) -> Unit = {}
    ): ResolvedUiError {
        val resolvedError = resolveUiError(
            error = error,
            context = context,
            consumeUxAction = consumeUxAction
        )
        applyFieldErrors(resolvedError.fieldErrors)
        return resolvedError
    }

    protected fun emitErrorSnackbar(
        error: AppError,
        context: ErrorContext = ErrorContext.Generic
    ) {
        val resolvedError = resolveUiError(error = error, context = context, consumeUxAction = false)
        showSnackbar(resolvedError.message)
    }

    private fun consumeResolvedUxAction(resolvedError: ResolvedUiError) {
        when (resolvedError.uxAction) {
            UiErrorUxAction.SHOW_SNACKBAR -> showSnackbar(resolvedError.message)
            UiErrorUxAction.SHOW_DIALOG,
            UiErrorUxAction.NAVIGATE_AUTH,
            UiErrorUxAction.NONE -> Unit
        }
    }

    protected fun emitCommonUiEvent(event: CommonUiEvent) {
        _commonUiEvents.emitUiEffect(event)
    }

    protected fun <E> MutableSharedFlow<E>.emitUiEffect(effect: E) {
        if (!tryEmit(effect)) {
            launch {
                emit(effect)
            }
        }
    }

    private companion object {
        const val TAG = "BaseViewModel"
    }
}

