package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.presentation.common.dialog.DialogModel

/**
 * Uygulama genelinde, tek bir noktada ([GlobalUiEventBus]) tüketilen olaylar.
 *
 * Ekrana özel (per-ViewModel) snackbar/dialog akışından
 * ([com.mefy.platemate.presentation.common.messaging.UiMessage]) farklı olarak;
 * sunucuya ulaşılamaması, oturum süresinin dolması gibi tüm uygulamayı ilgilendiren
 * kritik olaylar bu kanaldan akar ve
 * [com.mefy.platemate.presentation.navigation.AppNavHost] tarafından tek elden gösterilir.
 */
sealed interface GlobalAppEvent {

    /** Tüm uygulamayı bloklayan bir hata pop-up'ı gösterir (ör. sunucuya ulaşılamadı). */
    data class ShowGlobalDialog(val dialog: DialogModel) : GlobalAppEvent

    /** Oturum/token geçersiz; kullanıcı yeniden giriş ekranına yönlendirilmeli. */
    data object SessionExpired : GlobalAppEvent
}
