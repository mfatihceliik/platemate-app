package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.repository.DiscoveryRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val discoveryRepository: DiscoveryRepository
) {
    suspend operator fun invoke() {
        repository.logout()
        // Onceki oturumun premium/feed onbellegi yeni girise sizmasin.
        discoveryRepository.clearCache()
    }
}
