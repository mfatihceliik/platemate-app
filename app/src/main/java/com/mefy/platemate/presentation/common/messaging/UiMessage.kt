package com.mefy.platemate.presentation.common.messaging

import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.text.UiText

/**
 * Ekran-yerel (per-ViewModel) UI bildirimi: ekran açıkken üstten inen banner ile gösterilir.
 * Uygulama-geneli kritik olaylar (oturum bitti) için bkz.
 * [com.mefy.platemate.presentation.common.global.GlobalAppEvent].
 */
sealed interface UiMessage {
    data class ShowSnackbar(
        val message: UiText,
        val severity: BannerSeverity = BannerSeverity.Info
    ) : UiMessage
}
