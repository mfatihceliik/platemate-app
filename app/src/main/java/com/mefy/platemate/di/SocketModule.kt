package com.mefy.platemate.di

import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSourceImpl

import com.mefy.platemate.data.remote.websocket.datasource.SocketAuthTokenProvider
import com.mefy.platemate.data.remote.websocket.datasource.SocketAuthTokenProviderImpl
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSourceImpl
import com.mefy.platemate.data.remote.websocket.datasource.SocketNotificationDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketNotificationDataSourceImpl
import com.mefy.platemate.data.remote.websocket.ChatLiveSync
import com.mefy.platemate.data.remote.websocket.ChatLiveSyncImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {

    @Binds
    @Singleton
    abstract fun bindSocketConnectionService(
        impl: SocketConnectionDataSourceImpl
    ): SocketConnectionDataSource

    @Binds
    @Singleton
    abstract fun bindSocketMessagingService(
        impl: SocketMessagingDataSourceImpl
    ): SocketMessagingDataSource

    @Binds
    @Singleton
    abstract fun bindSocketNotificationService(
        impl: SocketNotificationDataSourceImpl
    ): SocketNotificationDataSource

    @Binds
    @Singleton
    abstract fun bindSocketAuthTokenProvider(
        impl: SocketAuthTokenProviderImpl
    ): SocketAuthTokenProvider

    @Binds
    @Singleton
    abstract fun bindChatLiveSync(
        impl: ChatLiveSyncImpl
    ): ChatLiveSync
}


