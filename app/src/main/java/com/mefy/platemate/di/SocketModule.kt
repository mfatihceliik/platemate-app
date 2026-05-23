package com.mefy.platemate.di

import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSourceImpl
import com.mefy.platemate.data.remote.websocket.datasource.SocketLocationDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketLocationDataSourceImpl
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSourceImpl
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
    abstract fun bindSocketLocationService(
        impl: SocketLocationDataSourceImpl
    ): SocketLocationDataSource
}


