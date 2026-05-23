package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryHome

interface DiscoveryRepository {
    suspend fun getDiscoveryHome(forceRefresh: Boolean = false): AppResult<DiscoveryHome>
}
