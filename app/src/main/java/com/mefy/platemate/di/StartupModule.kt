package com.mefy.platemate.di

import com.mefy.platemate.core.startup.AppCoordinator
import com.mefy.platemate.core.startup.FcmRegistrationCoordinator
import com.mefy.platemate.core.startup.InAppNotificationCoordinator
import com.mefy.platemate.core.startup.LiveMessageCacheCoordinator
import com.mefy.platemate.core.startup.SocketLifecycleCoordinator
import com.mefy.platemate.core.startup.SocketReconnectCoordinator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

/** App-geneli yan etki koordinatörlerini tek bir [Set] olarak toplar (open-closed). */
@Module
@InstallIn(SingletonComponent::class)
abstract class StartupModule {

    @Binds
    @IntoSet
    abstract fun bindSocketLifecycleCoordinator(impl: SocketLifecycleCoordinator): AppCoordinator

    @Binds
    @IntoSet
    abstract fun bindFcmRegistrationCoordinator(impl: FcmRegistrationCoordinator): AppCoordinator

    @Binds
    @IntoSet
    abstract fun bindLiveMessageCacheCoordinator(impl: LiveMessageCacheCoordinator): AppCoordinator

    @Binds
    @IntoSet
    abstract fun bindSocketReconnectCoordinator(impl: SocketReconnectCoordinator): AppCoordinator

    @Binds
    @IntoSet
    abstract fun bindInAppNotificationCoordinator(impl: InAppNotificationCoordinator): AppCoordinator
}
