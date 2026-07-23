package com.mefy.platemate.domain.usecase.discovery

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryTabFilter
import com.mefy.platemate.domain.model.discovery.DiscoveryTabPage
import com.mefy.platemate.domain.repository.DiscoveryRepository
import javax.inject.Inject

class GetDiscoveryTabFeedUseCase @Inject constructor(
    private val repository: DiscoveryRepository
) {
    suspend operator fun invoke(
        tab: String,
        filter: DiscoveryTabFilter = DiscoveryTabFilter(),
        page: Int,
        size: Int = DEFAULT_PAGE_SIZE
    ): AppResult<DiscoveryTabPage> {
        return repository.getTabFeed(tab = tab, filter = filter, page = page, size = size)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
