package com.mefy.platemate.domain.usecase.discovery

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.CityPlatePage
import com.mefy.platemate.domain.repository.DiscoveryRepository
import javax.inject.Inject

class GetDiscoveryCityPlatesUseCase @Inject constructor(
    private val repository: DiscoveryRepository
) {
    suspend operator fun invoke(cityId: Int, page: Int, size: Int = DEFAULT_PAGE_SIZE): AppResult<CityPlatePage> {
        return repository.getCityPlates(cityId = cityId, page = page, size = size)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
