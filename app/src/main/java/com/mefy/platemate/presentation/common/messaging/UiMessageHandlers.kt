package com.mefy.platemate.presentation.common.messaging

import androidx.compose.runtime.Composable
import com.mefy.platemate.presentation.app.providers.LocalUiMessageHandlers
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

data class UiMessageHandlers(
    val onShowSnackbar: (UiText, BannerSeverity) -> Unit,
    val onShowDialog: (DialogModel) -> Unit
)

@Composable
fun HandleUiMessages(messages: Flow<UiMessage>) {
    val handlers = LocalUiMessageHandlers.current
    CollectUiMessages(
        messages = messages,
        onShowSnackbar = handlers.onShowSnackbar,
        onShowDialog = handlers.onShowDialog
    )
}
