package com.mefy.platemate.presentation.common.dialog

import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText

/**
 * Tek noktadan ortak dialog kurucu. Hata/oturum-bitti gibi tekrar eden dialog'lar
 * burada üretilir; böylece [com.mefy.platemate.presentation.navigation.AppNavHost] ve
 * [com.mefy.platemate.presentation.common.viewmodel.BaseViewModel] aynı kaynağı kullanır.
 */
object DialogFactory {

    /** Genel hata pop-up'ı (ör. sunucuya ulaşılamadı / çevrimdışı). */
    fun errorDialog(message: UiText): DialogModel = DialogModel(
        title = UiText.Resource(R.string.common_error_title),
        message = message,
        confirmText = UiText.Resource(R.string.common_ok),
        variant = DialogVariant.Error
    )

    /** Oturum süresi doldu; onaylanınca yeniden giriş akışına yönlendirir. */
    fun sessionExpiredDialog(onConfirm: () -> Unit): DialogModel = DialogModel(
        title = UiText.Resource(R.string.session_expired_title),
        message = UiText.Resource(R.string.common_error_unauthorized),
        confirmText = UiText.Resource(R.string.session_expired_action_login),
        dismissible = false,
        variant = DialogVariant.Error,
        onConfirm = onConfirm
    )
}
