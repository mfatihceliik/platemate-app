package com.mefy.platemate.presentation.common.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

@Composable
fun CollectUiMessages(
    messages: Flow<UiMessage>,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (DialogModel) -> Unit
) {
    LaunchedEffect(messages) {
        messages.collect { message ->
            when (message) {
                is UiMessage.ShowSnackbar -> onShowSnackbar(message.message)
                is UiMessage.ShowDialog -> onShowDialog(message.dialog)
            }
        }
    }
}
