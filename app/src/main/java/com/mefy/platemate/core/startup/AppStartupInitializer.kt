package com.mefy.platemate.core.startup

import com.mefy.platemate.core.notification.channel.NotificationChannelRegistry
import com.mefy.platemate.core.notification.foreground.AppForegroundState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Uygulama açılışının tek giriş noktası: bildirim kanallarını oluşturur, foreground izlemeyi
 * başlatır ve tüm [AppCoordinator]'ları çalıştırır. [com.mefy.platemate.PlateMateApp.onCreate]
 * içinde bir kez çağrılır.
 */
@Singleton
class AppStartupInitializer @Inject constructor(
    private val coordinators: Set<@JvmSuppressWildcards AppCoordinator>,
    private val channelRegistry: NotificationChannelRegistry,
    private val foregroundState: AppForegroundState
) {
    fun initialize() {
        channelRegistry.ensureChannels()
        foregroundState.register()
        coordinators.forEach(AppCoordinator::start)
    }
}
