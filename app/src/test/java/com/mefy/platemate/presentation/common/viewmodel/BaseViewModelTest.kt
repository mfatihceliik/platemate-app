package com.mefy.platemate.presentation.common.viewmodel

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.event.CommonDialogModel
import com.mefy.platemate.presentation.common.event.CommonUiEvent
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
    fun showSnackbar_emitsCommonSnackbarEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.commonUiEvents.first() }
        runCurrent()

        viewModel.triggerSnackbar(UiText.Dynamic("Hello"))
        advanceUntilIdle()

        assertEquals(
            CommonUiEvent.ShowSnackbar(UiText.Dynamic("Hello")),
            eventDeferred.await()
        )
    }

    @Test
    fun showDialog_emitsCommonDialogEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val dialog = CommonDialogModel(
            title = UiText.Dynamic("Title"),
            message = UiText.Dynamic("Message"),
            confirmText = UiText.Dynamic("OK")
        )
        val eventDeferred = async { viewModel.commonUiEvents.first() }
        runCurrent()

        viewModel.triggerDialog(dialog)
        advanceUntilIdle()

        assertEquals(CommonUiEvent.ShowDialog(dialog), eventDeferred.await())
    }

    @Test
    fun emitErrorSnackbar_mapsAppErrorToSnackbarEvent() = runTest {
        val viewModel = TestBaseViewModel()
        val eventDeferred = async { viewModel.commonUiEvents.first() }
        runCurrent()

        viewModel.triggerError(AppError.Unknown("Something went wrong"))
        advanceUntilIdle()

        assertEquals(
            CommonUiEvent.ShowSnackbar(UiText.Resource(R.string.common_error_unknown)),
            eventDeferred.await()
        )
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

    private class TestBaseViewModel : BaseViewModel() {

        fun triggerSnackbar(message: UiText) {
            showSnackbar(message)
        }

        fun triggerDialog(dialog: CommonDialogModel) {
            showDialog(dialog)
        }

        fun triggerError(error: AppError) {
            emitErrorSnackbar(error)
        }

        fun runLaunch(
            onError: (Throwable) -> Unit = {},
            block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit
        ) = launch(onError = onError, block = block)
    }
}

