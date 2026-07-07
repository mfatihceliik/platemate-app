package com.mefy.platemate.presentation.common.banner

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Üstten inen banner'ın tek doğru kaynağı. Push bildirimleri ve UI mesajları (hata/başarı) hepsi
 * [show] ile buraya yazar; kök composable [current] + [resetKey]'i [com.mefy.platemate.presentation
 * .components.PMInAppNotificationBanner]'a verir. "Latest-wins" (aynı anda tek banner).
 */
@Stable
class InAppBannerController {

    var current by mutableStateOf<InAppBannerUiModel?>(null)
        private set

    /** Her [show]'da artar; banner auto-dismiss zamanlayıcısını yeniden tetikler. */
    var resetKey by mutableIntStateOf(0)
        private set

    fun show(banner: InAppBannerUiModel) {
        current = banner
        resetKey++
    }

    fun dismiss() {
        current = null
    }
}
