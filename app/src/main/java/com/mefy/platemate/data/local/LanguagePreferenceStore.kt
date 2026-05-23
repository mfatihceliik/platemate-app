package com.mefy.platemate.data.local

import com.mefy.platemate.domain.model.language.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LanguagePreferenceStore {
    fun observeLanguage(): Flow<AppLanguage?>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun getLanguageOrNull(): AppLanguage?
    fun peekLanguageOrNull(): AppLanguage?
}

