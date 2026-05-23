package com.mefy.platemate.domain.usecase.location

import com.mefy.platemate.domain.repository.LocationRepository
import javax.inject.Inject

class GetBlockedLocationUsersUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke() = repository.getBlockedLocationUsers()
}
