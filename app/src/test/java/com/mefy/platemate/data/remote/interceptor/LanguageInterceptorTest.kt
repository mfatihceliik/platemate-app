package com.mefy.platemate.data.remote.interceptor

import com.mefy.platemate.data.remote.language.LanguageProvider
import java.util.concurrent.atomic.AtomicReference
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class LanguageInterceptorTest {

    @Test
    fun interceptor_addsAcceptLanguageHeader_whenMissing() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeLanguageProvider("tr"), capturedRequest)

        client.newCall(Request.Builder().url("http://localhost/api/users").build()).execute().close()

        assertEquals("tr", capturedRequest.get().header("Accept-Language"))
    }

    @Test
    fun interceptor_keepsExistingAcceptLanguageHeader() {
        val capturedRequest = AtomicReference<Request>()
        val client = clientWith(FakeLanguageProvider("tr"), capturedRequest)

        client.newCall(
            Request.Builder()
                .url("http://localhost/api/users")
                .header("Accept-Language", "en")
                .build()
        ).execute().close()

        assertEquals("en", capturedRequest.get().header("Accept-Language"))
    }

    private fun clientWith(
        languageProvider: LanguageProvider,
        capturedRequest: AtomicReference<Request>
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(LanguageInterceptor(languageProvider))
        .addInterceptor { chain ->
            capturedRequest.set(chain.request())
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }
        .build()

    private class FakeLanguageProvider(
        private val language: String
    ) : LanguageProvider {
        override fun getAcceptLanguage(): String = language
    }
}
