package com.mefy.platemate.presentation.common.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class CommonDialogHostState internal constructor() {

    var activeDialog: CommonDialogModel? by mutableStateOf(null)
        private set

    fun showDialog(dialog: CommonDialogModel) {
        activeDialog = dialog
    }

    fun dismissDialog() {
        activeDialog = null
    }

    fun dismissOnRequest() {
        if (activeDialog?.dismissible == true) {
            dismissDialog()
        }
    }
}

@Composable
fun rememberCommonDialogHostState(): CommonDialogHostState {
    return remember { CommonDialogHostState() }
}
