package com.mefy.platemate.di

import com.mefy.platemate.data.local.NotificationPermissionStore
import com.mefy.platemate.data.local.datastore.DataStoreNotificationPermissionStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationPermissionModule {

    @Binds
    @Singleton
    abstract fun bindNotificationPermissionStore(
        impl: DataStoreNotificationPermissionStore
    ): NotificationPermissionStore
}
