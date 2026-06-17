package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.theme.AppThemeMode
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val themePreferenceStore: ThemePreferenceStore
) {
    suspend operator fun invoke(themeMode: AppThemeMode) {
        themePreferenceStore.setThemeMode(themeMode)
    }
}
