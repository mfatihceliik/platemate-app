package com.mefy.platemate.di

import com.mefy.platemate.presentation.common.error.DefaultUiErrorResolver
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UiErrorModule {

    @Binds
    @Singleton
    abstract fun bindUiErrorResolver(impl: DefaultUiErrorResolver): UiErrorResolver
}
