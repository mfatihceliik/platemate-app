package com.mefy.platemate.di

import com.mefy.platemate.data.local.datastore.DataStoreSessionStore
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.repository.AuthRepositoryImpl
import com.mefy.platemate.data.repository.ChatRepositoryImpl

import com.mefy.platemate.data.repository.ProfileRepositoryImpl
import com.mefy.platemate.data.repository.PlateRepositoryImpl
import com.mefy.platemate.data.repository.PlateReviewRepositoryImpl
import com.mefy.platemate.data.repository.DiscoveryRepositoryImpl
import com.mefy.platemate.data.repository.RoomRecentSearchRepository
import com.mefy.platemate.data.repository.RoomSavedPlateRepository
import com.mefy.platemate.data.repository.SettingsRepositoryImpl
import com.mefy.platemate.data.repository.SocialRepositoryImpl
import com.mefy.platemate.data.repository.SocialLinkRepositoryImpl
import com.mefy.platemate.data.repository.UserRepositoryImpl
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.repository.ChatRepository
import com.mefy.platemate.domain.repository.DiscoveryRepository

import com.mefy.platemate.domain.repository.PlateRepository
import com.mefy.platemate.domain.repository.ProfileRepository
import com.mefy.platemate.domain.repository.PlateReviewRepository
import com.mefy.platemate.domain.repository.RecentSearchRepository
import com.mefy.platemate.domain.repository.SavedPlateRepository
import com.mefy.platemate.domain.repository.SettingsRepository
import com.mefy.platemate.domain.repository.SocialRepository
import com.mefy.platemate.domain.repository.SocialLinkRepository
import com.mefy.platemate.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionStore(impl: DataStoreSessionStore): SessionStore

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(impl: PlateReviewRepositoryImpl): PlateReviewRepository

    @Binds
    @Singleton
    abstract fun bindPlateRepository(impl: PlateRepositoryImpl): PlateRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindSocialRepository(impl: SocialRepositoryImpl): SocialRepository

    @Binds
    @Singleton
    abstract fun bindSocialLinkRepository(impl: SocialLinkRepositoryImpl): SocialLinkRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository


    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindDiscoveryRepository(impl: DiscoveryRepositoryImpl): DiscoveryRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(impl: RoomRecentSearchRepository): RecentSearchRepository

    @Binds
    @Singleton
    abstract fun bindSavedPlateRepository(impl: RoomSavedPlateRepository): SavedPlateRepository

}
