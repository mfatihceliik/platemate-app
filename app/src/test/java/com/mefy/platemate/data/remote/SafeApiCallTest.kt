package com.mefy.platemate.data.remote

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.result.AppResult
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
    fun safeApiCall_returnsApiError_whenBackendMarksFailure() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = "No plate", success = false, data = null) }

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.Api("No plate"), (result as AppResult.Error).error)
    }

    @Test
    fun safeApiCall_returnsNetworkError_whenSuccessBodyHasNoData() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = null, success = true, data = null) }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun safeApiCall_mapsHttp401ToSessionExpired() = runBlocking {
        val errorBody = "Unauthorized".toResponseBody("text/plain".toMediaType())
        val exception = HttpException(Response.error<String>(401, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertEquals(AppResult.Error(AppError.SessionExpired), result)
    }

    @Test
    fun safeApiCall_mapsHttp403ToApiErrorWithMessage() = runBlocking {
        val jsonBody = """{"success":false,"message":"You are not authorized to perform this action."}"""
        val errorBody = jsonBody.toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(403, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Api)
        assertEquals("You are not authorized to perform this action.", (error as AppError.Api).message)
    }

    @Test
    fun safeApiCall_extractsErrorCodeFromDataField() = runBlocking {
        val jsonBody = """{"success":false,"message":"Refresh token is expired.","data":{"code":"REFRESH_EXPIRED"}}"""
        val errorBody = jsonBody.toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(400, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Api)
        assertEquals("REFRESH_EXPIRED", (error as AppError.Api).errorCode)
    }

    @Test
    fun safeApiCall_excludesCodeKeyFromFieldErrors() = runBlocking {
        val jsonBody = """{"success":false,"message":"Error","data":{"code":"REFRESH_EXPIRED","email":"invalid"}}"""
        val errorBody = jsonBody.toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(400, errorBody))

        val result = safeApiCall<String> { throw exception }

        val error = (result as AppResult.Error).error as AppError.Api
        assertEquals("REFRESH_EXPIRED", error.errorCode)
        assertEquals(mapOf("email" to "invalid"), error.fieldErrors)
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
    fun safeApiCall_maps4xxErrorBodyEnvelopeToApiError() = runBlocking {
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
        assertTrue(error is AppError.Api)
        val apiError = error as AppError.Api
        assertEquals("Validation failed", apiError.message)
        assertEquals("Must be at least 1", apiError.fieldErrors?.get("rating"))
        assertEquals("Cannot be blank", apiError.fieldErrors?.get("comment"))
    }

    @Test
    fun safeApiCall_mapsHttp5xxToNetworkError() = runBlocking {
        val errorBody = "not-json".toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(500, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun safeApiCall_mapsConnectExceptionToNetworkErrorWithCause() = runBlocking {
        val connectException = ConnectException("Connection refused")

        val result = safeApiCall<String> { throw connectException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Network)
        assertEquals(connectException, (error as AppError.Network).cause)
    }

    @Test
    fun safeApiCall_mapsSocketTimeoutToNetworkError() = runBlocking {
        val timeoutException = SocketTimeoutException("connect timed out")

        val result = safeApiCall<String> { throw timeoutException }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun safeApiCall_mapsUnknownHostToNetworkErrorOffline() = runBlocking {
        val unknownHostException = UnknownHostException("no DNS")

        val result = safeApiCall<String> { throw unknownHostException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Network)
        assertTrue((error as AppError.Network).isOffline)
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
    fun safeApiCall_mapsUnknownExceptionToNetworkErrorWithCause() = runBlocking {
        val illegalStateException = IllegalStateException("Boom")

        val result = safeApiCall<String> { throw illegalStateException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Network)
        assertEquals(illegalStateException, (error as AppError.Network).cause)
    }

    @Test
    fun safeApiCall_mapsParseFailureToNetworkError() = runBlocking {
        val parsingException = JsonSyntaxException("Malformed response payload")

        val result = safeApiCall<String> { throw parsingException }

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).error is AppError.Network)
    }

    @Test
    fun safeResultCall_returnsUnit_whenBackendSucceeds() = runBlocking {
        val result = safeResultCall { ResultResponse(message = "done", success = true) }

        assertEquals(AppResult.Success(Unit), result)
    }

    @Test
    fun safeResultCall_returnsApiError_whenBackendFails() = runBlocking {
        val result = safeResultCall { ResultResponse(message = "failed", success = false) }

        assertEquals(AppResult.Error(AppError.Api("failed")), result)
    }
}
