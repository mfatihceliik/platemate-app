package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.location.UserLocation
import kotlinx.coroutines.flow.Flow

interface SocketLocationDataSource {
    fun observeLocations(): Flow<UserLocation>
}

