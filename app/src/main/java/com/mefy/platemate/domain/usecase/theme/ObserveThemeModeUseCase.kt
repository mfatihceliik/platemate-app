package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.data.local.ThemePreferenceStore
import com.mefy.platemate.domain.model.theme.AppThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveThemeModeUseCase @Inject constructor(
    private val themePreferenceStore: ThemePreferenceStore
) {
    operator fun invoke(): Flow<AppThemeMode> = themePreferenceStore.observeThemeMode()
}
