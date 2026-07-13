package com.mefy.platemate.domain.usecase.settings

import com.mefy.platemate.data.local.CardStylePreferenceStore
import com.mefy.platemate.domain.model.settings.PlateCardStyle
import javax.inject.Inject

class SetPlateCardStyleUseCase @Inject constructor(
    private val cardStylePreferenceStore: CardStylePreferenceStore
) {
    suspend operator fun invoke(style: PlateCardStyle) {
        cardStylePreferenceStore.setCardStyle(style)
    }
}
