package com.mefy.platemate.core.notification.foreground

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Uygulamanın foreground/background durumunu süreç-geneli izler. Bildirimler yalnızca app arka
 * plandayken gösterildiği için [com.mefy.platemate.core.notification.AppNotificationManager] bunu
 * okur; [foregroundTransitions] ise app her foreground'a dönüşte yayın yapar (socket reconnect +
 * resync için). [register] ana iş parçacığında, [com.mefy.platemate.PlateMateApp.onCreate] içinde
 * bir kez çağrılmalı.
 */
@Singleton
class AppForegroundState @Inject constructor() : DefaultLifecycleObserver {

    @Volatile
    var isForeground: Boolean = false
        private set

    private val _foregroundTransitions = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /** App background → foreground geçişlerinde (her `onStart`) bir sinyal yayınlar. */
    val foregroundTransitions: SharedFlow<Unit> = _foregroundTransitions.asSharedFlow()

    fun register() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        isForeground = true
        _foregroundTransitions.tryEmit(Unit)
    }

    override fun onStop(owner: LifecycleOwner) {
        isForeground = false
    }
}
