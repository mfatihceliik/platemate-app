package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.data.local.ThemePreferenceStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAccentColorUseCase @Inject constructor(
    private val themePreferenceStore: ThemePreferenceStore
) {
    operator fun invoke(): Flow<Long> = themePreferenceStore.observeAccentColorArgb()
}
