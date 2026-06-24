package com.mefy.platemate.presentation.common.global

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [GlobalUiEventBus] varsayılan uygulaması.
 *
 * `replay = 0`, geç abone olan bir tüketicinin eski olayları tekrar görmesini engeller;
 * `extraBufferCapacity` ile abone yokken bile [emit] kayıpsız çalışır.
 */
@Singleton
class DefaultGlobalUiEventBus @Inject constructor() : GlobalUiEventBus {

    private val _events = MutableSharedFlow<GlobalAppEvent>(
        replay = 0,
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val events: SharedFlow<GlobalAppEvent> = _events.asSharedFlow()

    override fun emit(event: GlobalAppEvent) {
        _events.tryEmit(event)
    }
}
