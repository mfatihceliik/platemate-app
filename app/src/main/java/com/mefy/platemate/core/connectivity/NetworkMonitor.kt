package com.mefy.platemate.core.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {

    val isOnline: Flow<Boolean>

    fun isCurrentlyOnline(): Boolean
}
