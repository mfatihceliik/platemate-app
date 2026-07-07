package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.domain.repository.ThemeRepository
import javax.inject.Inject

class SyncAppearanceUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(themeMode: AppThemeMode, accentHex: String) =
        repository.syncAppearance(themeMode, accentHex)
}
