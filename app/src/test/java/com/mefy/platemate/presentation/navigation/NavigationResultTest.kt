package com.mefy.platemate.presentation.navigation

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class NavigationResultTest {

    @Test
    fun consumeOneShotResult_returnsValueThenRemovesIt() {
        val handle = SavedStateHandle()
        handle.setOneShotResult(key = "snackbar", value = "Saved")

        val firstConsume = handle.consumeOneShotResult<String>("snackbar")
        val secondConsume = handle.consumeOneShotResult<String>("snackbar")

        assertEquals("Saved", firstConsume)
        assertNull(secondConsume)
        assertFalse(handle.contains("snackbar"))
    }

    @Test
    fun consumeOneShotResult_removesNullableValue() {
        val handle = SavedStateHandle()
        handle.setOneShotResult<String?>(key = "nullable", value = null)

        val consumed = handle.consumeOneShotResult<String?>("nullable")

        assertNull(consumed)
        assertFalse(handle.contains("nullable"))
    }
}
