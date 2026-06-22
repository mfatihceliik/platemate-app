package com.mefy.platemate.presentation.common.messaging

import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText

/**
 * Ekran-yerel (per-ViewModel) UI bildirimi: ekran açıkken gösterilecek snackbar
 * veya dialog. Uygulama-geneli kritik olaylar için bkz.
 * [com.mefy.platemate.presentation.common.global.GlobalAppEvent].
 */
sealed interface UiMessage {
    data class ShowSnackbar(val message: UiText) : UiMessage
    data class ShowDialog(val dialog: DialogModel) : UiMessage
}
