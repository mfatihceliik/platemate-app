package com.mefy.platemate.domain.usecase.location

import com.mefy.platemate.domain.repository.LocationRepository
import javax.inject.Inject

class ObserveLocationUpdatesUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke() = repository.observeLocationUpdates()
}
