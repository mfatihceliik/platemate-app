package com.mefy.platemate.di

import com.mefy.platemate.data.local.CardStylePreferenceStore
import com.mefy.platemate.data.local.datastore.DataStoreCardStylePreferenceStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CardStyleModule {

    @Binds
    @Singleton
    abstract fun bindCardStylePreferenceStore(impl: DataStoreCardStylePreferenceStore): CardStylePreferenceStore
}
