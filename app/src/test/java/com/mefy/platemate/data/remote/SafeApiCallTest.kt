package com.mefy.platemate.data.remote

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.google.gson.JsonSyntaxException
import java.io.IOException
import java.net.ConnectException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun safeApiCall_returnsBackendError_whenBackendMarksFailure() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = "No plate", success = false, data = null) }

        assertTrue(result is AppResult.Error)
        assertEquals(AppError.Backend("No plate"), (result as AppResult.Error).error)
    }

    @Test
    fun safeApiCall_returnsEmptyData_whenSuccessBodyHasNoData() = runBlocking {
        val result = safeApiCall<String> { DataResultResponse(message = null, success = true, data = null) }

        assertEquals(AppResult.Error(AppError.EmptyData), result)
    }

    @Test
    fun safeApiCall_mapsHttp401ToUnauthorized() = runBlocking {
        val errorBody = "Unauthorized".toResponseBody("text/plain".toMediaType())
        val exception = HttpException(Response.error<String>(401, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertEquals(AppResult.Error(AppError.Unauthorized), result)
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
    fun safeApiCall_mapsHttpErrorBodyEnvelopeToHttpAppError() = runBlocking {
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
        assertTrue(error is AppError.Http)
        val httpError = error as AppError.Http
        assertEquals(400, httpError.code)
        assertEquals("Validation failed", httpError.message)
        assertEquals("Validation failed", httpError.backendMessage)
        assertEquals("Must be at least 1", httpError.fieldErrors?.get("rating"))
        assertEquals("Cannot be blank", httpError.fieldErrors?.get("comment"))
    }

    @Test
    fun safeApiCall_keepsFallbackWhenHttpErrorBodyIsNotParseable() = runBlocking {
        val errorBody = "not-json".toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<String>(500, errorBody))

        val result = safeApiCall<String> { throw exception }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Http)
        val httpError = error as AppError.Http
        assertEquals(500, httpError.code)
        assertEquals("not-json", httpError.rawBody)
        assertNull(httpError.fieldErrors)
    }

    @Test
    fun safeApiCall_mapsConnectExceptionToServerUnavailableWithCause() = runBlocking {
        val connectException = ConnectException("Connection refused")

        val result = safeApiCall<String> { throw connectException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.ServerUnavailable)
        val serverUnavailableError = error as AppError.ServerUnavailable
        assertEquals("Connection refused", serverUnavailableError.message)
        assertEquals(connectException, serverUnavailableError.cause)
    }

    @Test
    fun safeApiCall_mapsIOExceptionToNetworkErrorWithCause() = runBlocking {
        val ioException = IOException("No internet")

        val result = safeApiCall<String> { throw ioException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Network)
        val networkError = error as AppError.Network
        assertEquals("No internet", networkError.message)
        assertEquals(ioException, networkError.cause)
    }

    @Test
    fun safeApiCall_mapsUnknownExceptionToUnknownErrorWithCause() = runBlocking {
        val illegalStateException = IllegalStateException("Boom")

        val result = safeApiCall<String> { throw illegalStateException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Unknown)
        val unknownError = error as AppError.Unknown
        assertEquals("Boom", unknownError.message)
        assertEquals(illegalStateException, unknownError.cause)
    }

    @Test
    fun safeApiCall_mapsGsonStructureMismatchToSerializationError() = runBlocking {
        val parsingException = IllegalStateException(
            "Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 284 path $.data.recentReviews"
        )

        val result = safeApiCall<String> { throw parsingException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Serialization)
        val serializationError = error as AppError.Serialization
        assertEquals(parsingException.message, serializationError.message)
        assertEquals(parsingException, serializationError.cause)
    }

    @Test
    fun safeApiCall_mapsJsonSyntaxExceptionToSerializationError() = runBlocking {
        val parsingException = JsonSyntaxException("Malformed response payload")

        val result = safeApiCall<String> { throw parsingException }

        assertTrue(result is AppResult.Error)
        val error = (result as AppResult.Error).error
        assertTrue(error is AppError.Serialization)
        val serializationError = error as AppError.Serialization
        assertEquals("Malformed response payload", serializationError.message)
        assertEquals(parsingException, serializationError.cause)
    }

    @Test
    fun safeMessageCall_returnsUnit_whenBackendSucceeds() = runBlocking {
        val result = safeMessageCall { ResultResponse(message = "done", success = true) }

        assertEquals(AppResult.Success(Unit), result)
    }

    @Test
    fun safeMessageCall_returnsBackendError_whenBackendFails() = runBlocking {
        val result = safeMessageCall { ResultResponse(message = "failed", success = false) }

        assertEquals(AppResult.Error(AppError.Backend("failed")), result)
    }
}

