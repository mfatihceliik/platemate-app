package com.mefy.platemate.presentation.common.event

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.mefy.platemate.presentation.common.text.resolve

@Composable
fun CommonDialogHost(
    state: CommonDialogHostState
) {
    val dialog = state.activeDialog ?: return

    AlertDialog(
        onDismissRequest = state::dismissOnRequest,
        title = { Text(text = dialog.title.resolve()) },
        text = { Text(text = dialog.message.resolve()) },
        confirmButton = {
            TextButton(
                onClick = state::dismissDialog
            ) {
                Text(text = dialog.confirmText.resolve())
            }
        },
        dismissButton = dialog.dismissText?.let { dismissText ->
            {
                TextButton(
                    onClick = state::dismissDialog
                ) {
                    Text(text = dismissText.resolve())
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = dialog.dismissible,
            dismissOnClickOutside = dialog.dismissible
        )
    )
}
