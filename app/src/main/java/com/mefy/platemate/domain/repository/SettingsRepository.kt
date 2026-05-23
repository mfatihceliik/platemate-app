package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.settings.UserSettings

interface SettingsRepository {
    suspend fun getMySettings(): AppResult<UserSettings>
    suspend fun updateSettings(settings: UserSettings): AppResult<Unit>
}


