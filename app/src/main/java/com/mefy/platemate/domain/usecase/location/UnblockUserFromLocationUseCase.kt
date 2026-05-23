package com.mefy.platemate.domain.usecase.location

import com.mefy.platemate.domain.repository.LocationRepository
import javax.inject.Inject

class UnblockUserFromLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(targetUserId: Long) = repository.unblockUserFromLocation(targetUserId)
}
