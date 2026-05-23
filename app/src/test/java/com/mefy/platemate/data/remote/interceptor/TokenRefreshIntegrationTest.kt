package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.remote.rest.service.AuthTokenApiService
import com.mefy.platemate.domain.model.auth.AuthSession
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class TokenRefreshIntegrationTest {

    @Test
    fun protectedRequest_retriesAfterRefresh_andSucceeds() = runBlocking {
        val refreshCalls = AtomicInteger(0)
        val server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when {
                        request.path?.startsWith("/api/auth/refresh") == true -> {
                            refreshCalls.incrementAndGet()
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(
                                    """
                                    {
                                      "success": true,
                                      "message": null,
                                      "data": {
                                        "id": 1,
                                        "username": "mfy",
                                        "email": "mfy@test.com",
                                        "token": "new-access",
                                        "refreshToken": "new-refresh"
                                      }
                                    }
                                    """.trimIndent()
                                )
                        }
                        request.path?.startsWith("/api/profiles/1") == true -> {
                            if (request.getHeader("Authorization") == "Bearer new-access") {
                                MockResponse().setResponseCode(200)
                            } else {
                                MockResponse().setResponseCode(401)
                            }
                        }
                        else -> MockResponse().setResponseCode(404)
                    }
                }
            }
            start()
        }

        try {
            val sessionStore = FakeSessionStore(
                AuthSession(
                    userId = 1L,
                    username = "mfy",
                    token = "old-access",
                    refreshToken = "old-refresh"
                )
            )
            val api = createProtectedApi(server, sessionStore)

            val response = api.getProfile()

            assertEquals(200, response.code())
            assertEquals(1, refreshCalls.get())
            assertEquals("new-access", sessionStore.session.first()?.token)
            assertEquals("new-refresh", sessionStore.session.first()?.refreshToken)
        } finally {
            server.shutdown()
        }
    }

    @Test
    fun protectedRequest_whenRefreshUnauthorized_keeps401AndClearsSession() = runBlocking {
        val server = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when {
                        request.path?.startsWith("/api/auth/refresh") == true ->
                            MockResponse()
                                .setResponseCode(401)
                                .setBody(
                                    """
                                    {
                                      "success": false,
                                      "message": "refresh expired",
                                      "data": { "code": "REFRESH_EXPIRED" }
                                    }
                                    """.trimIndent()
                                )

                        request.path?.startsWith("/api/profiles/1") == true ->
                            MockResponse().setResponseCode(401)

                        else -> MockResponse().setResponseCode(404)
                    }
                }
            }
            start()
        }

        try {
            val sessionStore = FakeSessionStore(
                AuthSession(
                    userId = 1L,
                    username = "mfy",
                    token = "old-access",
                    refreshToken = "old-refresh"
                )
            )
            val api = createProtectedApi(server, sessionStore)

            val response = api.getProfile()

            assertEquals(401, response.code())
            assertNull(sessionStore.session.first())
        } finally {
            server.shutdown()
        }
    }

    private fun createProtectedApi(
        server: MockWebServer,
        sessionStore: SessionStore
    ): ProtectedApi {
        val refreshRetrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        val refreshApiService = refreshRetrofit.create(AuthTokenApiService::class.java)

        val protectedClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionStore))
            .authenticator(
                TokenAuthenticator(
                    sessionStore = sessionStore,
                    authTokenApiService = refreshApiService,
                    refreshFailureClassifier = RefreshFailureClassifier()
                )
            )
            .build()
        val protectedRetrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(protectedClient)
            .build()
        return protectedRetrofit.create(ProtectedApi::class.java)
    }

    private interface ProtectedApi {
        @GET("api/profiles/1")
        suspend fun getProfile(): Response<Unit>
    }

    private class FakeSessionStore(initialSession: AuthSession?) : SessionStore {
        private val state = MutableStateFlow(initialSession)
        override val session: Flow<AuthSession?> = state

        override suspend fun saveSession(session: AuthSession) {
            state.value = session
        }

        override suspend fun clearSession() {
            state.value = null
        }

        override suspend fun getToken(): String? = state.value?.token

        override fun peekToken(): String? = state.value?.token

        override suspend fun getRefreshToken(): String? = state.value?.refreshToken

        override fun peekRefreshToken(): String? = state.value?.refreshToken
    }
}
