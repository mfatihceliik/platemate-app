package com.mefy.platemate.domain.usecase.settings

import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: UserSettings) = repository.updateSettings(settings)
}


