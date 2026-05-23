package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.domain.model.socket.SocketConnectionState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow

@Singleton
class SocketConnectionDataSourceImpl @Inject constructor(
    private val connectionManager: SocketConnectionManager
) : SocketConnectionDataSource {

    override suspend fun connect() = connectionManager.connect()

    override suspend fun disconnect() = connectionManager.disconnect()

    override fun isConnected(): Boolean = connectionManager.isConnected()

    override fun observeConnectionState(): StateFlow<SocketConnectionState> =
        connectionManager.observeConnectionState()
}


