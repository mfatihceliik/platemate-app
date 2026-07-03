package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [InAppNotificationBus] varsayılan uygulaması. `replay = 0` geç abonenin eski banner'ları
 * tekrar görmesini engeller; `extraBufferCapacity` ile kısa abonelik boşluklarında [emit] kayıpsız.
 */
@Singleton
class DefaultInAppNotificationBus @Inject constructor() : InAppNotificationBus {

    private val _events = MutableSharedFlow<AppNotification>(
        replay = 0,
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: SharedFlow<AppNotification> = _events.asSharedFlow()

    override fun emit(notification: AppNotification) {
        _events.tryEmit(notification)
    }
}
