package com.mefy.platemate.di

import com.mefy.platemate.data.local.datastore.DataStoreThemePreferenceStore
import com.mefy.platemate.data.local.ThemePreferenceStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeModule {

    @Binds
    @Singleton
    abstract fun bindThemePreferenceStore(impl: DataStoreThemePreferenceStore): ThemePreferenceStore
}
