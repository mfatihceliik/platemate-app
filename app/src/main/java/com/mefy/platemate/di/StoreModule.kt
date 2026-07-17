package com.mefy.platemate.di

import com.mefy.platemate.data.local.RecentUserSearchStore
import com.mefy.platemate.data.local.datastore.DataStoreRecentUserSearchStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StoreModule {
    @Binds
    @Singleton
    fun bindRecentUserSearchStore(
        impl: DataStoreRecentUserSearchStore
    ): RecentUserSearchStore
}
