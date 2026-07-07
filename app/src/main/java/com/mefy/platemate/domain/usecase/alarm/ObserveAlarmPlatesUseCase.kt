package com.mefy.platemate.domain.usecase.alarm

import com.mefy.platemate.domain.repository.AlarmPlateRepository
import javax.inject.Inject

class ObserveAlarmPlatesUseCase @Inject constructor(
    private val repository: AlarmPlateRepository
) {
    operator fun invoke() = repository.observeAlarmPlates()
}
