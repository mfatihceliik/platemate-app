package com.mefy.platemate.core.startup

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.data.remote.websocket.ChatLiveSync
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Oturum açıkken tüm odalardan gelen canlı mesaj/durumu Room'a (tek doğru kaynak) yazar; oturum
 * kapanınca durur. UI Room'u dinlediği için liste/sohbet hangi ekranda olunursa olsun güncel kalır.
 */
@Singleton
class LiveMessageCacheCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val chatLiveSync: ChatLiveSync,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun start() {
        scope.launch {
            observeSessionUseCase()
                .flatMapLatest { session ->
                    if (session == null) emptyFlow() else chatLiveSync.updates()
                }
                .collect { }
        }
    }
}
