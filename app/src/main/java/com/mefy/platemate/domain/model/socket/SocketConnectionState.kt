package com.mefy.platemate.domain.model.socket

enum class SocketConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    UNAUTHORIZED,
    ERROR
}
