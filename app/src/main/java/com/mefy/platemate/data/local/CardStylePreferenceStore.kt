package com.mefy.platemate.data.local

import com.mefy.platemate.domain.model.settings.PlateCardStyle
import kotlinx.coroutines.flow.Flow

interface CardStylePreferenceStore {
    fun observeCardStyle(): Flow<PlateCardStyle>
    suspend fun setCardStyle(style: PlateCardStyle)
    fun peekCardStyle(): PlateCardStyle
}
