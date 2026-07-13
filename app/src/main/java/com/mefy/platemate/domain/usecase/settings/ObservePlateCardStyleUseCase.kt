package com.mefy.platemate.domain.usecase.settings

import com.mefy.platemate.data.local.CardStylePreferenceStore
import com.mefy.platemate.domain.model.settings.PlateCardStyle
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObservePlateCardStyleUseCase @Inject constructor(
    private val cardStylePreferenceStore: CardStylePreferenceStore
) {
    operator fun invoke(): Flow<PlateCardStyle> = cardStylePreferenceStore.observeCardStyle()
}
