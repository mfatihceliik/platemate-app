package com.mefy.platemate.core.startup

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Oturum açıkken soketi uygulama-geneli ayağa kaldırır, kapanınca koparır. Böylece presence
 * (online durum) ve canlı mesaj akışı, kullanıcı bir konuşma ekranında olmasa da çalışır.
 */
@Singleton
class SocketLifecycleCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val socketConnectionDataSource: SocketConnectionDataSource,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    override fun start() {
        scope.launch {
            observeSessionUseCase().collect { session ->
                if (session != null) {
                    socketConnectionDataSource.connect()
                } else {
                    socketConnectionDataSource.disconnect()
                }
            }
        }
    }
}
