package com.mefy.platemate.di

import com.mefy.platemate.core.util.Constants.DEFAULT_API_ENDPOINT
import com.mefy.platemate.core.util.Constants.DEFAULT_SOCKET_ENDPOINT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("ApiEndpoint")
    fun provideApiEndpoint(): String = DEFAULT_API_ENDPOINT

    @Provides
    @Named("WebSocketEndpoint")
    fun provideWebSocketEndpoint(): String = DEFAULT_SOCKET_ENDPOINT
}
