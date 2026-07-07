package com.mefy.platemate.data.fcm

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.usecase.fcm.RegisterFcmTokenUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * FCM token yenilendiğinde, oturum aktifse backend'e kaydeder (auth interceptor bearer ekler).
 * Token kaydı sorumluluğu, mesaj render'ından ayrı tutulur.
 */
@Singleton
class FcmTokenRegistrar @Inject constructor(
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val sessionStore: SessionStore,
    @param:ApplicationScope private val scope: CoroutineScope
) {
    fun onNewToken(token: String) {
        scope.launch {
            if (!sessionStore.peekToken().isNullOrBlank()) {
                registerFcmTokenUseCase(token)
            }
        }
    }
}
