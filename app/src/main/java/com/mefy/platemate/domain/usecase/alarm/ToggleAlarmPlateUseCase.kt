package com.mefy.platemate.domain.usecase.alarm

import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.repository.AlarmPlateRepository
import javax.inject.Inject

class ToggleAlarmPlateUseCase @Inject constructor(
    private val repository: AlarmPlateRepository
) {
    suspend operator fun invoke(plate: AlarmPlate) = repository.toggleAlarm(plate)
}
