package com.mefy.platemate.presentation.common.messaging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

/**
 * Ekran-yerel ([com.mefy.platemate.presentation.common.viewmodel.BaseViewModel.uiMessages])
 * bildirimlerinin kök seviyedeki tek banner sink'ine bağlanmasını sağlayan işleyici.
 *
 * [com.mefy.platemate.presentation.app.AppShell] bunu bir kez [LocalUiMessageHandlers]
 * üzerinden sağlar; nav katmanındaki `screenComposable` helper'ı her hedef için [HandleUiMessages]'i
 * bir kez çağırarak o ekranın ViewModel akışını toplar (rotalar artık çağırmaz).
 */
data class UiMessageHandlers(
    val onShowSnackbar: (UiText, BannerSeverity) -> Unit,
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
