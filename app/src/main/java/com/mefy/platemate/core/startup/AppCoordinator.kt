package com.mefy.platemate.core.startup

/**
 * Uygulama başlangıcında bir kez başlatılan, süreç-geneli (Activity/ViewModel'den bağımsız)
 * yan etki koordinatörü. Tüm implementasyonlar [AppStartupInitializer] tarafından başlatılır.
 */
interface AppCoordinator {
    fun start()
}
