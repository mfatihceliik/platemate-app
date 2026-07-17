package com.mefy.platemate.core.util

object Constants {
    // NOTE: REST and Socket must point at the SAME host. Change both together.
    // Real device on LAN: use the machine's LAN IP (below). Emulator: use 10.0.2.2.
    private const val HOST = "79.76.57.48" // "192.168.1.116" // emulator: "10.0.2.2"
    const val DEFAULT_API_ENDPOINT = "http://$HOST:8080/"
    const val DEFAULT_SOCKET_ENDPOINT = "http://$HOST:9092/"
}
