package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.socket.SocketConnectionState
import kotlinx.coroutines.flow.StateFlow

interface SocketConnectionDataSource {
    suspend fun connect()
    suspend fun disconnect()
    fun isConnected(): Boolean
    fun observeConnectionState(): StateFlow<SocketConnectionState>
}

