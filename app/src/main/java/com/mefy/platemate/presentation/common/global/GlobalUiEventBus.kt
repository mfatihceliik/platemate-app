package com.mefy.platemate.presentation.common.global

import kotlinx.coroutines.flow.SharedFlow

/**
 * Uygulama ömrü boyunca yaşayan tek global olay kanalının soyutlaması.
 *
 * Yayıncılar (her [com.mefy.platemate.presentation.common.viewmodel.BaseViewModel]) ile
 * tek tüketici ([com.mefy.platemate.presentation.navigation.AppNavHost]) arasını gevşek
 * bağlar. ViewModel'ler yeniden yaratılsa da bu kanal süreç boyunca aynı kalır; böylece
 * hangi ekranda olunursa olunsun kritik (sunucuya ulaşılamadı / oturum bitti) olaylar tek
 * noktadan gösterilir.
 *
 * Statik `object` yerine arayüz + DI ([javax.inject.Singleton]) kullanılır; bu sayede test
 * içinde sahte (fake) bir uygulama enjekte edilebilir.
 */
interface GlobalUiEventBus {

    val events: SharedFlow<GlobalAppEvent>

    fun emit(event: GlobalAppEvent)
}
