package com.mefy.platemate.core.common.result

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
    fun onSuccess_runsOnlyForSuccessBranch() {
        var successSideEffect = false

        AppResult.Success(1)
            .onSuccess { successSideEffect = true }

        assertTrue(successSideEffect)
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
