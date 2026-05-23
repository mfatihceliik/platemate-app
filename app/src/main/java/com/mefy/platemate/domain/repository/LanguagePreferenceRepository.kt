package com.mefy.platemate.domain.repository

import com.mefy.platemate.domain.model.language.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LanguagePreferenceRepository {
    fun observeLanguage(): Flow<AppLanguage?>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun getLanguageOrNull(): AppLanguage?
    fun peekLanguageOrNull(): AppLanguage?
}

