package com.mefy.platemate.core.common

import com.mefy.platemate.core.error.AppError
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppResultExtensionsTest {

    @Test
    fun map_transformsSuccessValue() {
        val result = AppResult.Success(3).map { it * 2 }

        assertEquals(AppResult.Success(6), result)
    }

    @Test
    fun flatMap_transformsSuccessResult() {
        val result = AppResult.Success(7).flatMap { AppResult.Success(it.toString()) }

        assertEquals(AppResult.Success("7"), result)
    }

    @Test
    fun fold_returnsMappedOutputForSuccessAndError() {
        val successOutput = AppResult.Success("ok").fold(
            onSuccess = { "S:$it" },
            onError = { "E:${it.message}" }
        )
        val errorOutput = AppResult.Error(AppError.Backend("bad")).fold(
            onSuccess = { "S:$it" },
            onError = { "E:${it.message}" }
        )

        assertEquals("S:ok", successOutput)
        assertEquals("E:bad", errorOutput)
    }

    @Test
    fun mapError_transformsErrorOnly() {
        val result = AppResult.Error(AppError.Backend("x")).mapError {
            AppError.Backend("mapped")
        }

        assertEquals(AppResult.Error(AppError.Backend("mapped")), result)
    }

    @Test
    fun onSuccess_and_onError_runOnlyForMatchingBranch() {
        var successSideEffect = false
        var errorSideEffect = false

        AppResult.Success(1)
            .onSuccess { successSideEffect = true }
            .onError { errorSideEffect = true }

        assertTrue(successSideEffect)
        assertTrue(!errorSideEffect)
    }

    @Test
    fun flatMapSuspend_and_onSuccessSuspend_supportSuspendTransformations() = runBlocking {
        var captured = 0
        val result = AppResult.Success(5)
            .flatMapSuspend { AppResult.Success(it + 2) }
            .onSuccessSuspend { captured = it }

        assertEquals(AppResult.Success(7), result)
        assertEquals(7, captured)
    }
}
