package com.mefy.platemate.core.notification

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Ekranda **açık olan tek** sohbet odasının id'sini uygulama-geneli tutar (telefonda aynı anda tek
 * sohbet açıktır). Yalnızca o odanın gereksiz mesaj bildirimini bastırmak ([AppNotificationManager]),
 * unread rozetini artırmamak ve listede aktif odanın rozetini bastırmak
 * ([com.mefy.platemate.data.repository.ChatRepositoryImpl]) için kullanılır. Hangi odaların
 * **dinlendiğini kısıtlamaz** — tüm odalar `ChatLiveSync` üzerinden dinlenip Room'a yazılır.
 *
 * [activeRoomId] anlık (senkron) okuma için; [activeRoomIdFlow] reaktif tüketiciler içindir.
 */
@Singleton
class ActiveConversationTracker @Inject constructor() {

    private val _activeRoomId = MutableStateFlow<Long?>(null)
    val activeRoomIdFlow: StateFlow<Long?> = _activeRoomId.asStateFlow()

    var activeRoomId: Long?
        get() = _activeRoomId.value
        set(value) { _activeRoomId.value = value }
}
