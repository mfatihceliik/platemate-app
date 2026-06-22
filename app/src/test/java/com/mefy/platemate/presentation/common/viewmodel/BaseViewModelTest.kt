package com.mefy.platemate.presentation.common.viewmodel

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.dialog.DialogModel
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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun showSnackbar_emitsCommonSnackbarEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerSnackbar(UiText.Dynamic("Hello"))
        advanceUntilIdle()

        assertEquals(
            UiMessage.ShowSnackbar(UiText.Dynamic("Hello")),
            eventDeferred.await()
        )
    }

    @Test
    fun showDialog_emitsCommonDialogEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val dialog = DialogModel(
            title = UiText.Dynamic("Title"),
            message = UiText.Dynamic("Message"),
            confirmText = UiText.Dynamic("OK")
        )
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerDialog(dialog)
        advanceUntilIdle()

        assertEquals(UiMessage.ShowDialog(dialog), eventDeferred.await())
    }

    @Test
    fun emitErrorSnackbar_mapsAppErrorToSnackbarEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.uiMessages.first() }
        runCurrent()

        viewModel.triggerError(AppError.Server(message = null))
        advanceUntilIdle()

        assertEquals(
            UiMessage.ShowSnackbar(UiText.Resource(R.string.common_error_unknown)),
            eventDeferred.await()
        )
    }

    @Test
    fun handleError_connectivityFailure_emitsGlobalDialog() = runTest {
        val bus = DefaultGlobalUiEventBus()
        val viewModel = TestBaseViewModel(bus)
        val eventDeferred = async { bus.events.first() }
        runCurrent()

        viewModel.triggerHandled(AppError.Unreachable())
        advanceUntilIdle()

        assertTrue(eventDeferred.await() is GlobalAppEvent.ShowGlobalDialog)
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

        fun triggerSnackbar(message: UiText) {
            showSnackbar(message)
        }

        fun triggerDialog(dialog: DialogModel) {
            showDialog(dialog)
        }

        fun triggerError(error: AppError) {
            handleError(error)
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

