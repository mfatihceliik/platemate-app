package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.model.auth.AuthSession
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthInterceptorTest {

    @Test
    fun interceptor_addsBearerToken_whenTokenExists() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeSessionStore(token = "abc123"), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/users").build()).execute().close()

        assertEquals("Bearer abc123", capturedRequest.get().header("Authorization"))
    }

    @Test
    fun interceptor_doesNotAddHeader_whenTokenMissing() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeSessionStore(token = null), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/users").build()).execute().close()

        assertNull(capturedRequest.get().header("Authorization"))
    }

    @Test
    fun interceptor_skipsAuthEndpoints() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeSessionStore(token = "abc123"), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/auth/login").build()).execute().close()

        assertNull(capturedRequest.get().header("Authorization"))
    }

    @Test
    fun interceptor_skipsLogoutEndpoint() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeSessionStore(token = "abc123"), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/auth/logout").build()).execute().close()

        assertNull(capturedRequest.get().header("Authorization"))
    }

    @Test
    fun interceptor_skipsPublicCityEndpoint_andAddsAuthForPlateSearch() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeSessionStore(token = "abc123"), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/cities/34").build()).execute().close()
        assertNull(capturedRequest.get().header("Authorization"))

        client.newCall(Request.Builder().url("http://localhost/api/plates/search?plate=34ABC123").build()).execute().close()
        assertEquals("Bearer abc123", capturedRequest.get().header("Authorization"))
    }

    @Test
    fun interceptor_doesNotClearSession_whenResponseIsUnauthorized() {
        val capturedRequest = AtomicReference<Request>()
        val sessionStore = FakeSessionStore(token = "abc123")
        val client = clientWith(sessionStore, capturedRequest, responseCode = 401)

        client.newCall(Request.Builder().url("http://localhost/api/users").build()).execute().close()

        assertEquals(0, sessionStore.clearSessionCallCount)
    }

    private fun clientWith(
        sessionStore: SessionStore,
        capturedRequest: AtomicReference<Request>,
        responseCode: Int = 200
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionStore))
        .addInterceptor { chain ->
            capturedRequest.set(chain.request())
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(responseCode)
                .message("OK")
                .build()
        }
        .build()

    private class FakeSessionStore(
        private val token: String?
    ) : SessionStore {
        var clearSessionCallCount: Int = 0

        override val session: Flow<AuthSession?> = flowOf(
            token?.let {
                AuthSession(
                    userId = 1,
                    username = "mfy",
                    token = it
                )
            }
        )

        override suspend fun saveSession(session: AuthSession) = Unit
        override suspend fun clearSession() {
            clearSessionCallCount++
        }
        override suspend fun getToken(): String? = token
        override fun peekToken(): String? = token
        override suspend fun getRefreshToken(): String? = null
        override fun peekRefreshToken(): String? = null
    }
}
