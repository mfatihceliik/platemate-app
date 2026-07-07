package com.mefy.platemate.data.remote.interceptor

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshFailureClassifier @Inject constructor() {

    @Suppress("UNUSED_PARAMETER")
    fun shouldClearSession(statusCode: Int, rawBody: String?): Boolean {
        return statusCode == HTTP_UNAUTHORIZED
    }

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
