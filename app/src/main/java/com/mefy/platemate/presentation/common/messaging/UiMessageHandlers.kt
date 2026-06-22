package com.mefy.platemate.presentation.common.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

/**
 * Ekran-yerel ([com.mefy.platemate.presentation.common.viewmodel.BaseViewModel.uiMessages])
 * snackbar/dialog bildirimlerinin kök seviyedeki tek sink'e (snackbar host + dialog host)
 * bağlanmasını sağlayan işleyiciler.
 *
 * [com.mefy.platemate.presentation.navigation.AppNavHost] bunları bir kez
 * [LocalUiMessageHandlers] üzerinden sağlar; her ekran yalnızca [HandleUiMessages]
 * çağırarak kendi ViewModel akışını toplar. Böylece param drilling ortadan kalkar ve
 * snackbar her ekranda çalışır.
 */
data class UiMessageHandlers(
    val onShowSnackbar: (UiText) -> Unit,
    val onShowDialog: (DialogModel) -> Unit
)

val LocalUiMessageHandlers = staticCompositionLocalOf<UiMessageHandlers> {
    error("LocalUiMessageHandlers sağlanmadı; AppNavHost içinde CompositionLocalProvider ile sarılmalı.")
}

/** İçinde bulunulan ekranın ViewModel'inden gelen yerel UI bildirimlerini kök sink'e toplar. */
@Composable
fun HandleUiMessages(messages: Flow<UiMessage>) {
    val handlers = LocalUiMessageHandlers.current
    CollectUiMessages(
        messages = messages,
        onShowSnackbar = handlers.onShowSnackbar,
        onShowDialog = handlers.onShowDialog
    )
}
