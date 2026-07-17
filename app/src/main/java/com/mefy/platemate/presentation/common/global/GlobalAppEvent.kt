package com.mefy.platemate.presentation.common.global

/**
 * Uygulama genelinde, tek bir noktada ([GlobalUiEventBus]) tüketilen kritik olaylar.
 *
 * Hata/başarı gibi geçici geri-bildirimler artık üstten inen banner ile gösterilir
 * ([com.mefy.platemate.presentation.common.messaging.UiMessage]); bu kanal yalnızca tüm
 * uygulamayı bloklaması gereken olaylar içindir (oturum bitti → zorunlu yeniden giriş) ve
 * [com.mefy.platemate.presentation.app.AppGlobalEffects] tarafından tek elden gösterilir.
 */
sealed interface GlobalAppEvent {

    /** Oturum/token geçersiz; kullanıcı bloklayan dialog'la yeniden giriş ekranına yönlendirilir. */
    data object SessionExpired : GlobalAppEvent
}
