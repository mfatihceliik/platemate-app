package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.flow.Flow

/** Soket `notification_received` akışı — uygulama-içi banner kaynağı (foreground). */
interface SocketNotificationDataSource {
    fun observeNotifications(): Flow<AppNotification>
}
