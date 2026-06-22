package com.mefy.platemate.presentation.common.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class DialogHostState internal constructor() {

    var activeDialog: DialogModel? by mutableStateOf(null)
        private set

    fun showDialog(dialog: DialogModel) {
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
fun rememberDialogHostState(): DialogHostState {
    return remember { DialogHostState() }
}
