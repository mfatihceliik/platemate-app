package com.mefy.platemate.di

import com.mefy.platemate.BuildConfig
import com.mefy.platemate.data.remote.interceptor.AuthInterceptor
import com.mefy.platemate.data.remote.interceptor.LanguageInterceptor
import com.mefy.platemate.data.remote.interceptor.TokenAuthenticator
import com.mefy.platemate.data.remote.rest.service.AuthApiService
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.data.remote.rest.service.ChatApiService
import com.mefy.platemate.data.remote.rest.service.DiscoveryApiService
import com.mefy.platemate.data.remote.rest.service.FcmTokenApiService
import com.mefy.platemate.data.remote.rest.service.LocationApiService
import com.mefy.platemate.data.remote.rest.service.PlateApiService
import com.mefy.platemate.data.remote.rest.service.ProfileApiService
import com.mefy.platemate.data.remote.rest.service.ReviewApiService
import com.mefy.platemate.data.remote.rest.service.SettingsApiService
import com.mefy.platemate.data.remote.rest.service.SocialApiService
import com.mefy.platemate.data.remote.rest.service.SocialLinkApiService
import com.mefy.platemate.data.remote.rest.service.SubscriptionApiService
import com.mefy.platemate.data.remote.rest.service.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        languageInterceptor: LanguageInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(languageInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("RefreshOkHttpClient")
    fun provideRefreshOkHttpClient(
        languageInterceptor: LanguageInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(languageInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("ApiEndpoint") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("RefreshRetrofit")
    fun provideRefreshRetrofit(
        @Named("ApiEndpoint") baseUrl: String,
        @Named("RefreshOkHttpClient") refreshOkHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(refreshOkHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthTokenApiService(
        @Named("RefreshRetrofit") refreshRetrofit: Retrofit
    ): AuthTokenApiService = refreshRetrofit.create(AuthTokenApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideSettingsApiService(retrofit: Retrofit): SettingsApiService =
        retrofit.create(SettingsApiService::class.java)

    @Provides
    @Singleton
    fun provideReviewApiService(retrofit: Retrofit): ReviewApiService =
        retrofit.create(ReviewApiService::class.java)

    @Provides
    @Singleton
    fun providePlateApiService(retrofit: Retrofit): PlateApiService =
        retrofit.create(PlateApiService::class.java)
    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)

    @Provides
    @Singleton
    fun provideSocialApiService(retrofit: Retrofit): SocialApiService =
        retrofit.create(SocialApiService::class.java)

    @Provides
    @Singleton
    fun provideSocialLinkApiService(retrofit: Retrofit): SocialLinkApiService =
        retrofit.create(SocialLinkApiService::class.java)

    @Provides
    @Singleton
    fun provideLocationApiService(retrofit: Retrofit): LocationApiService =
        retrofit.create(LocationApiService::class.java)

    @Provides
    @Singleton
    fun provideSubscriptionApiService(retrofit: Retrofit): SubscriptionApiService =
        retrofit.create(SubscriptionApiService::class.java)

    @Provides
    @Singleton
    fun provideFcmTokenApiService(retrofit: Retrofit): FcmTokenApiService =
        retrofit.create(FcmTokenApiService::class.java)

    @Provides
    @Singleton
    fun provideDiscoveryApiService(retrofit: Retrofit): DiscoveryApiService =
        retrofit.create(DiscoveryApiService::class.java)
}
