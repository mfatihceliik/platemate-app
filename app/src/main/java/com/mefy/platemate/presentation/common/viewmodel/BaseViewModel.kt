package com.mefy.platemate.presentation.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.messaging.UiMessage
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
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
    // Üretimde tüm ViewModel'ler Hilt'ten tek (singleton) örneği enjekte eder; varsayılan
    // yalnızca birim testlerin no-arg kurabilmesi içindir.
    private val globalUiEventBus: GlobalUiEventBus = DefaultGlobalUiEventBus()
) : ViewModel() {

    private companion object {
        const val TAG = "BaseViewModel"
    }

    private val _uiMessages = MutableSharedFlow<UiMessage>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiMessages: SharedFlow<UiMessage> = _uiMessages.asSharedFlow()

    /**
     * Tek hata giriş noktası. UX, hatanın türünden belirlenir:
     * - [AppError.SessionExpired] -> uygulama-geneli yeniden giriş akışı
     * - [AppError.Network] / [AppError.Unreachable] -> bloklayan pop-up
     * - [AppError.Server] -> backend mesajıyla snackbar
     */
    protected fun handleError(error: AppError) {
        when (error) {
            AppError.SessionExpired ->
                globalUiEventBus.emit(GlobalAppEvent.SessionExpired)

            is AppError.Network, is AppError.Unreachable ->
                globalUiEventBus.emit(
                    GlobalAppEvent.ShowGlobalDialog(DialogFactory.errorDialog(error.toUiText()))
                )

            is AppError.Server ->
                showSnackbar(error.toUiText())
        }
    }

    /** Beklenmeyen ([Throwable]) hatalar için loglama; [launch] varsayılan onError'ı. */
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
        emitUiMessage(UiMessage.ShowSnackbar(message))
    }

    protected fun showDialog(dialog: DialogModel) {
        emitUiMessage(UiMessage.ShowDialog(dialog))
    }

    protected fun emitUiMessage(event: UiMessage) {
        _uiMessages.emitUiEffect(event)
    }

    protected fun <E> MutableSharedFlow<E>.emitUiEffect(effect: E) {
        if (!tryEmit(effect)) {
            launch {
                emit(effect)
            }
        }
    }
}
