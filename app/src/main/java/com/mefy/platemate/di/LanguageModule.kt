package com.mefy.platemate.di

import com.mefy.platemate.data.local.datastore.DataStoreLanguagePreferenceStore
import com.mefy.platemate.data.local.LanguagePreferenceStore
import com.mefy.platemate.data.remote.language.LanguageProvider
import com.mefy.platemate.data.remote.language.UserPreferredLanguageProvider
import com.mefy.platemate.data.repository.LanguagePreferenceRepositoryImpl
import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LanguageModule {

    @Binds
    @Singleton
    abstract fun bindLanguagePreferenceStore(impl: DataStoreLanguagePreferenceStore): LanguagePreferenceStore

    @Binds
    @Singleton
    abstract fun bindLanguagePreferenceRepository(impl: LanguagePreferenceRepositoryImpl): LanguagePreferenceRepository

    @Binds
    @Singleton
    abstract fun bindLanguageProvider(impl: UserPreferredLanguageProvider): LanguageProvider
}
