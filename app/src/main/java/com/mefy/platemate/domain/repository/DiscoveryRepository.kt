package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.CityPlatePage
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabFilter
import com.mefy.platemate.domain.model.discovery.DiscoveryTabPage
import com.mefy.platemate.domain.model.discovery.DiscoveryTabType

interface DiscoveryRepository {
    suspend fun getDiscoveryHome(forceRefresh: Boolean = false): AppResult<DiscoveryHome>

    suspend fun getCityPlates(cityId: Int, page: Int, size: Int): AppResult<CityPlatePage>

    suspend fun getTabFeed(
        tab: DiscoveryTabType,
        filter: DiscoveryTabFilter,
        page: Int,
        size: Int
    ): AppResult<DiscoveryTabPage>

    /** Oturum degistiginde (logout) onbellekteki home payload'i temizlenir; premium/feed sizmasi onlenir. */
    fun clearCache()
}
