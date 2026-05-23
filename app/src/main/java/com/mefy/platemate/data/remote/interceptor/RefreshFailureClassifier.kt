package com.mefy.platemate.data.remote.interceptor

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshFailureClassifier @Inject constructor() {

    @Suppress("UNUSED_PARAMETER")
    fun shouldClearSession(statusCode: Int, rawBody: String?): Boolean {
        return statusCode == HTTP_UNAUTHORIZED || statusCode == HTTP_FORBIDDEN
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
        const val HTTP_FORBIDDEN = 403
    }
}
