package com.mefy.platemate.di

import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.common.global.DefaultInAppNotificationBus
import com.mefy.platemate.presentation.common.global.DefaultNotificationNavigationBus
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.global.InAppNotificationBus
import com.mefy.platemate.presentation.common.global.NotificationNavigationBus
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventModule {

    @Binds
    @Singleton
    abstract fun bindGlobalUiEventBus(impl: DefaultGlobalUiEventBus): GlobalUiEventBus

    @Binds
    @Singleton
    abstract fun bindInAppNotificationBus(impl: DefaultInAppNotificationBus): InAppNotificationBus

    @Binds
    @Singleton
    abstract fun bindNotificationNavigationBus(impl: DefaultNotificationNavigationBus): NotificationNavigationBus
}
