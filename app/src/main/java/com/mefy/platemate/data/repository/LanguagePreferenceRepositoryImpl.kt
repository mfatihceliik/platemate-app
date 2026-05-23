package com.mefy.platemate.data.repository

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LanguagePreferenceRepositoryImpl @Inject constructor(
    private val store: LanguagePreferenceStore,
    private val appDispatchers: AppDispatchers
) : LanguagePreferenceRepository {

    override fun observeLanguage(): Flow<AppLanguage?> =
        store.observeLanguage().flowOn(appDispatchers.io)

    override suspend fun setLanguage(language: AppLanguage) {
        withContext(appDispatchers.io) {
            store.setLanguage(language)
        }
    }

    override suspend fun getLanguageOrNull(): AppLanguage? =
        withContext(appDispatchers.io) {
            store.getLanguageOrNull()
        }

    override fun peekLanguageOrNull(): AppLanguage? = store.peekLanguageOrNull()
}
