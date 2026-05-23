package com.mefy.platemate.domain.usecase.discovery

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.repository.DiscoveryRepository
import javax.inject.Inject

class GetDiscoveryHomeUseCase @Inject constructor(
    private val repository: DiscoveryRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): AppResult<DiscoveryHome> {
        return repository.getDiscoveryHome(forceRefresh = forceRefresh)
    }
}
