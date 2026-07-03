package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.settings.SettingsOverview
import com.mefy.platemate.domain.model.settings.UserSettings

interface SettingsRepository {
    suspend fun getSettingsOverview(): AppResult<SettingsOverview>
    suspend fun updateSettings(settings: UserSettings): AppResult<Unit>
}


