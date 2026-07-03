package com.mefy.platemate.presentation.common.viewmodel

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.messaging.UiMessage
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun showInfo_emitsInfoSnackbarMessage() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerInfo(UiText.Dynamic("Hello"))
        advanceUntilIdle()

        assertEquals(
            UiMessage.ShowSnackbar(UiText.Dynamic("Hello"), BannerSeverity.Info),
            eventDeferred.await()
        )
    }

    @Test
    fun showSuccess_emitsSuccessSnackbarMessage() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerSuccess(UiText.Dynamic("Done"))
        advanceUntilIdle()

        assertEquals(
            UiMessage.ShowSnackbar(UiText.Dynamic("Done"), BannerSeverity.Success),
            eventDeferred.await()
        )
    }

    @Test
    fun handleError_apiError_emitsErrorSnackbar() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerHandled(AppError.Api(message = null))
        advanceUntilIdle()

        assertEquals(
            UiMessage.ShowSnackbar(UiText.Resource(R.string.common_error_unknown), BannerSeverity.Error),
            eventDeferred.await()
        )
    }

    @Test
    fun handleError_networkError_emitsErrorSnackbar() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerHandled(AppError.Network())
        advanceUntilIdle()

        assertEquals(
            BannerSeverity.Error,
            (eventDeferred.await() as UiMessage.ShowSnackbar).severity
        )
    }

    @Test
    fun handleError_sessionExpired_emitsSessionExpired() = runTest {
        val bus = DefaultGlobalUiEventBus()
        val viewModel = TestBaseViewModel(bus)
        val eventDeferred = async { bus.events.first() }
        runCurrent()

        viewModel.triggerHandled(AppError.SessionExpired)
        advanceUntilIdle()

        assertEquals(GlobalAppEvent.SessionExpired, eventDeferred.await())
    }

    @Test
    fun launch_callsOnError_forNonCancellationExceptions() = runTest {
        val viewModel = TestBaseViewModel()
        var caught: Throwable? = null

        viewModel.runLaunch(onError = { caught = it }) {
            error("boom")
        }
        advanceUntilIdle()

        assertEquals("boom", caught?.message)
    }

    @Test
    fun launch_doesNotRouteCancellation_toOnError() = runTest {
        val viewModel = TestBaseViewModel()
        var onErrorCalled = false

        val job = viewModel.runLaunch(onError = { onErrorCalled = true }) {
            throw CancellationException("cancel")
        }
        advanceUntilIdle()

        assertFalse(onErrorCalled)
        job.cancel()
    }

    private class TestBaseViewModel(
        globalUiEventBus: GlobalUiEventBus = DefaultGlobalUiEventBus()
    ) : BaseViewModel(globalUiEventBus = globalUiEventBus) {

        fun triggerInfo(message: UiText) {
            showInfo(message)
        }

        fun triggerSuccess(message: UiText) {
            showSuccess(message)
        }

        fun triggerHandled(error: AppError) {
            handleError(error)
        }

        fun runLaunch(
            onError: (Throwable) -> Unit = {},
            block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit
        ) = launch(onError = onError, block = block)
    }
}
