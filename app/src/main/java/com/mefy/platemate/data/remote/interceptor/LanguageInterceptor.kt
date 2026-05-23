package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.remote.language.LanguageProvider
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor @Inject constructor(
    private val languageProvider: LanguageProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (request.header(ACCEPT_LANGUAGE_HEADER).isNullOrBlank()) {
            requestBuilder.header(ACCEPT_LANGUAGE_HEADER, languageProvider.getAcceptLanguage())
        }

        return chain.proceed(requestBuilder.build())
    }

    private companion object {
        private const val ACCEPT_LANGUAGE_HEADER = "Accept-Language"
    }
}
