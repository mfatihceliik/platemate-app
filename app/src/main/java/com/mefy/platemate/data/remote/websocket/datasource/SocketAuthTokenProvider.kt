package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.util.isAccessTokenExpired
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Socket el sıkışması için her zaman GEÇERLİ (süresi dolmamış) bir access token sağlar.
 *
 * REST tarafı 401'de [com.mefy.platemate.data.remote.interceptor.TokenAuthenticator] ile yenilenir;
 * socket query token'ı ise yenilenmediği için süresi dolduğunda handshake 401 alır ("xhr poll
 * error"). Bu sağlayıcı, bağlanmadan önce gerekiyorsa token'ı yeniler — mevcut yenileme akışını
 * ([AuthRepository.refreshSession]) yeniden kullanır.
 */
interface SocketAuthTokenProvider {
    suspend fun validAccessTokenOrNull(): String?
}

@Singleton
class SocketAuthTokenProviderImpl @Inject constructor(
    private val sessionStore: SessionStore,
    private val authRepository: AuthRepository
) : SocketAuthTokenProvider {

    override suspend fun validAccessTokenOrNull(): String? {
        val token = sessionStore.getToken()?.takeIf { it.isNotBlank() } ?: return null
        if (!token.isAccessTokenExpired()) return token

        return when (val result = authRepository.refreshSession()) {
            is AppResult.Success -> result.data.token.takeIf { it.isNotBlank() }
            is AppResult.Error -> null
        }
    }
}
