package com.mefy.platemate.presentation.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.banner.BannerSeverity
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
     * - [AppError.SessionExpired] -> uygulama-geneli yeniden giriş akışı (bloklayan dialog)
     * - [AppError.Network] / [AppError.Api] -> üstten inen kırmızı hata banner'ı
     */
    protected fun handleError(error: AppError) {
        when (error) {
            AppError.SessionExpired -> globalUiEventBus.emit(GlobalAppEvent.SessionExpired)
            is AppError.Network -> showError(error.toUiText())
            is AppError.Api -> showError(error.toUiText())
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

    /** Kırmızı hata banner'ı. */
    protected fun showError(message: UiText) {
        emitUiMessage(UiMessage.ShowSnackbar(message, BannerSeverity.Error))
    }

    /** Yeşil başarı banner'ı. */
    protected fun showSuccess(message: UiText) {
        emitUiMessage(UiMessage.ShowSnackbar(message, BannerSeverity.Success))
    }

    /** Nötr bilgi banner'ı. */
    protected fun showInfo(message: UiText) {
        emitUiMessage(UiMessage.ShowSnackbar(message, BannerSeverity.Info))
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
