package com.mefy.platemate.core.startup

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.chat.RecoverPendingMessagesUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Recovers PENDING messages orphaned by a process death mid-send (app killed before the ack or
 * 30s timeout could resolve them — see [com.mefy.platemate.data.repository.ChatRepositoryImpl]).
 * Runs once per user id becoming active (cold start with an existing session, or a fresh login),
 * not on every session re-emission (e.g. token refresh), since [distinctUntilChangedBy] only lets
 * a genuine user change through.
 */
@Singleton
class PendingMessageRecoveryCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val recoverPendingMessagesUseCase: RecoverPendingMessagesUseCase,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    override fun start() {
        scope.launch {
            observeSessionUseCase()
                .filterNotNull()
                .distinctUntilChangedBy { it.userId }
                .collect { recoverPendingMessagesUseCase() }
        }
    }
}
