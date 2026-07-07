package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.premium.PremiumCatalog

interface PremiumRepository {
    suspend fun getCatalog(): AppResult<PremiumCatalog>
}
