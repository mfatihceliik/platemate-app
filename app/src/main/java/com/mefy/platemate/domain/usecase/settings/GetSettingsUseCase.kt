package com.mefy.platemate.domain.usecase.settings

import com.mefy.platemate.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() = repository.getMySettings()
}
