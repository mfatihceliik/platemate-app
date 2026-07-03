package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.core.notification.model.AppNotification
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

@Singleton
class SocketNotificationDataSourceImpl @Inject constructor(
    private val connectionManager: SocketConnectionManager,
    private val mapper: SocketNotificationMapper
) : SocketNotificationDataSource {

    override fun observeNotifications(): Flow<AppNotification> =
        connectionManager.observeEvent(eventName = EVENT_NOTIFICATION_RECEIVED)
            .mapNotNull(mapper::map)

    private companion object {
        const val EVENT_NOTIFICATION_RECEIVED = "notification_received"
    }
}
