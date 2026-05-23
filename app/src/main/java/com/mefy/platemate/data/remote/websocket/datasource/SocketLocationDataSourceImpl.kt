package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.location.UserLocation
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.json.JSONObject

@Singleton
class SocketLocationDataSourceImpl @Inject constructor(
    private val connectionManager: SocketConnectionManager
) : SocketLocationDataSource {

    override fun observeLocations(): Flow<UserLocation> =
        connectionManager.observeEvent(eventName = "location_update")
            .mapNotNull { payload -> payload.toUserLocationOrNull() }

    private fun JSONObject.toUserLocationOrNull(): UserLocation? = runCatching {
        UserLocation(
            id = optLong("id"),
            userId = optLong("userId"),
            username = optNullableString("username") ?: "",
            latitude = optDouble("latitude"),
            longitude = optDouble("longitude"),
            lastUpdatedAt = optNullableString("lastUpdatedAt").toAppDateTimeOrNull()
        )
    }.getOrNull()

    private fun JSONObject.optNullableString(name: String): String? =
        if (has(name) && !isNull(name)) optString(name) else null

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let(::AppDateTime)
}


