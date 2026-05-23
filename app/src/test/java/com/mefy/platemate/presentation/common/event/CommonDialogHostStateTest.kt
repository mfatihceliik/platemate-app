package com.mefy.platemate.presentation.common.event

import com.mefy.platemate.presentation.common.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CommonDialogHostStateTest {

    @Test
    fun showDialog_setsActiveDialog() {
        val state = CommonDialogHostState()
        val dialog = testDialog(dismissible = true)

        state.showDialog(dialog)

        assertEquals(dialog, state.activeDialog)
    }

    @Test
    fun dismissDialog_clearsActiveDialog() {
        val state = CommonDialogHostState()
        state.showDialog(testDialog(dismissible = true))

        state.dismissDialog()

        assertNull(state.activeDialog)
    }

    @Test
    fun dismissOnRequest_dismissesWhenDismissible() {
        val state = CommonDialogHostState()
        state.showDialog(testDialog(dismissible = true))

        state.dismissOnRequest()

        assertNull(state.activeDialog)
    }

    @Test
    fun dismissOnRequest_keepsDialogWhenNotDismissible() {
        val state = CommonDialogHostState()
        val dialog = testDialog(dismissible = false)
        state.showDialog(dialog)

        state.dismissOnRequest()

        assertEquals(dialog, state.activeDialog)
    }

    private fun testDialog(dismissible: Boolean): CommonDialogModel {
        return CommonDialogModel(
            title = UiText.Dynamic("Title"),
            message = UiText.Dynamic("Message"),
            confirmText = UiText.Dynamic("OK"),
            dismissText = UiText.Dynamic("Cancel"),
            dismissible = dismissible
        )
    }
}
