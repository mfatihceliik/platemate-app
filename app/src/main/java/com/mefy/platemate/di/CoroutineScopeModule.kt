package com.mefy.platemate.di

import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.coroutine.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    /**
     * Uygulama-geneli, süreç ömründe yaşayan scope. Oturuma bağlı app-geneli yan etkiler
     * (socket lifecycle, FCM kaydı, canlı mesaj cache) bu scope'ta çalışır — Activity/ViewModel
     * yaşam döngüsünden bağımsız. [SupervisorJob] ile bir coroutine'in hatası diğerlerini düşürmez.
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(dispatchers: AppDispatchers): CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatchers.default)
}
