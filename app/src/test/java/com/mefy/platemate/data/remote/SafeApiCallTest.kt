package com.mefy.platemate.data.remote

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class SafeApiCallTest {

    @Test
    fun safeApiCall_returnsSuccessData_whenBackendSucceeds() = runBlocking {
        val result = safeApiCall { DataResultResponse(message = null, success = true, data = "ok") }

        assertEquals(AppResult.Success("ok"), result)
    }

    @Test
    fun safeApiCall_returnsServerError_whenBackendMarksFailure() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = "No plate", success = false, data = null) }

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.Server("No plate"), (result as AppResult.Error).error)
    }

    @Test
    fun safeApiCall_returnsUnreachable_whenSuccessBodyHasNoData() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = null, success = true, data = null) }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Unreachable)
    }

    @Test
    fun safeApiCall_mapsHttp401ToSessionExpired() = runBlocking {
        val errorBody = "Unauthorized".toResponseBody("text/plain".toMediaType())
        val exception = HttpException(Response.error<String>(401, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertEquals(AppResult.Error(AppError.SessionExpired), result)
    }

    @Test
    fun safeApiCall_mapsHttp403ToSessionExpired() = runBlocking {
        val errorBody = "Forbidden".toResponseBody("text/plain".toMediaType())
        val exception = HttpException(Response.error<String>(403, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertEquals(AppResult.Error(AppError.SessionExpired), result)
    }

    @Test
    fun safeApiCall_rethrowsCancellationException() = runBlocking {
        val exception = CancellationException("cancelled")

        try {
            safeApiCall<String> { throw exception }
            fail("Expected CancellationException to be rethrown")
        } catch (actual: CancellationException) {
            assertEquals("cancelled", actual.message)
        }
    }

    @Test
    fun safeApiCall_maps4xxErrorBodyEnvelopeToServerError() = runBlocking {
        val errorBody = """
            {
              "success": false,
              "message": "Validation failed",
              "data": {
                "rating": "Must be at least 1",
                "comment": "Cannot be blank"
              }
            }
        """.trimIndent().toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(400, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Server)
        val serverError = error as AppError.Server
        assertEquals("Validation failed", serverError.message)
        assertEquals("Must be at least 1", serverError.fieldErrors?.get("rating"))
        assertEquals("Cannot be blank", serverError.fieldErrors?.get("comment"))
    }

    @Test
    fun safeApiCall_mapsHttp5xxToUnreachable() = runBlocking {
        val errorBody = "not-json".toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(500, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Unreachable)
    }

    @Test
    fun safeApiCall_mapsConnectExceptionToUnreachableWithCause() = runBlocking {
        val connectException = ConnectException("Connection refused")

        val result = safeApiCall<String> { throw connectException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Unreachable)
        assertEquals(connectException, (error as AppError.Unreachable).cause)
    }

    @Test
    fun safeApiCall_mapsSocketTimeoutToUnreachable() = runBlocking {
        // Sunucu kapalı/yanıt vermiyor -> bağlantı zaman aşımı; sunucuya ulaşılamadı.
        val timeoutException = SocketTimeoutException("connect timed out")

        val result = safeApiCall<String> { throw timeoutException }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Unreachable)
    }

    @Test
    fun safeApiCall_mapsUnknownHostToNetworkError() = runBlocking {
        // DNS çözülemiyor -> cihaz büyük olasılıkla çevrimdışı; ağ hatası say.
        val unknownHostException = UnknownHostException("no DNS")

        val result = safeApiCall<String> { throw unknownHostException }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun safeApiCall_mapsIOExceptionToNetworkErrorWithCause() = runBlocking {
        val ioException = IOException("No internet")

        val result = safeApiCall<String> { throw ioException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Network)
        assertEquals(ioException, (error as AppError.Network).cause)
    }

    @Test
    fun safeApiCall_mapsUnknownExceptionToUnreachableWithCause() = runBlocking {
        val illegalStateException = IllegalStateException("Boom")

        val result = safeApiCall<String> { throw illegalStateException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Unreachable)
        assertEquals(illegalStateException, (error as AppError.Unreachable).cause)
    }

    @Test
    fun safeApiCall_mapsParseFailureToUnreachable() = runBlocking {
        val parsingException = JsonSyntaxException("Malformed response payload")

        val result = safeApiCall<String> { throw parsingException }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Unreachable)
    }

    @Test
    fun safeMessageCall_returnsUnit_whenBackendSucceeds() = runBlocking {
        val result = safeMessageCall { ResultResponse(message = "done", success = true) }

        assertEquals(AppResult.Success(Unit), result)
    }

    @Test
    fun safeMessageCall_returnsServerError_whenBackendFails() = runBlocking {
        val result = safeMessageCall { ResultResponse(message = "failed", success = false) }

        assertEquals(AppResult.Error(AppError.Server("failed")), result)
    }
}
