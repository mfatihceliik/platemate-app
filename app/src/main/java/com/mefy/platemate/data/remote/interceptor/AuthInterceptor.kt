package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.local.SessionStore
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val sessionStore: SessionStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (AuthPathPolicy.isPublicPath(request.url.encodedPath)) {
            return chain.proceed(request)
        }

        val token = sessionStore.peekToken()
        if (token.isNullOrBlank()) {
            return chain.proceed(request)
        }

        val authenticatedRequest = request.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }

    private companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}
