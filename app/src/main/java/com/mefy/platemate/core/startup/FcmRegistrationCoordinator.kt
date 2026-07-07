package com.mefy.platemate.core.startup

import com.google.firebase.messaging.FirebaseMessaging
import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.fcm.RegisterFcmTokenUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Oturum açıldığında mevcut FCM token'ı bir kez backend'e kaydeder; oturum kapanınca yeniden
 * kayda izin vermek için bayrağı sıfırlar. (Token yenileme akışı [com.mefy.platemate.data.fcm.FcmTokenRegistrar].)
 */
@Singleton
class FcmRegistrationCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    private var fcmRegistered = false

    override fun start() {
        scope.launch {
            observeSessionUseCase().collect { session ->
                when {
                    session != null && !fcmRegistered -> {
                        fcmRegistered = true
                        runCatching { FirebaseMessaging.getInstance().token.await() }
                            .getOrNull()
                            ?.takeIf { it.isNotBlank() }
                            ?.let { token -> registerFcmTokenUseCase(token) }
                    }
                    session == null -> fcmRegistered = false
                }
            }
        }
    }
}
