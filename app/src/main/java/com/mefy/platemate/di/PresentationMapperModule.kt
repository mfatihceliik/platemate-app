package com.mefy.platemate.di

import com.mefy.platemate.presentation.features.main.discover.mapper.DefaultDiscoverUiMapper
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverUiMapper
import com.mefy.platemate.presentation.features.main.profile.mapper.DefaultProfileUiMapper
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiMapper
import com.mefy.platemate.presentation.features.main.search.mapper.DefaultSearchUiMapper
import com.mefy.platemate.presentation.features.main.search.mapper.SearchUiMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PresentationMapperModule {

    @Binds
    @Singleton
    abstract fun bindDiscoverUiMapper(impl: DefaultDiscoverUiMapper): DiscoverUiMapper

    @Binds
    @Singleton
    abstract fun bindSearchUiMapper(impl: DefaultSearchUiMapper): SearchUiMapper

    @Binds
    @Singleton
    abstract fun bindProfileUiMapper(impl: DefaultProfileUiMapper): ProfileUiMapper
}
