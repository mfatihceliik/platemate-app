package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.data.local.ThemePreferenceStore
import javax.inject.Inject

class SetAccentColorUseCase @Inject constructor(
    private val themePreferenceStore: ThemePreferenceStore
) {
    suspend operator fun invoke(argb: Long) = themePreferenceStore.setAccentColorArgb(argb)
}
