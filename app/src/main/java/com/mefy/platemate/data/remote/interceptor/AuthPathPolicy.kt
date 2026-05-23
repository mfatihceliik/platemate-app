package com.mefy.platemate.data.remote.interceptor

object AuthPathPolicy {
    private val PUBLIC_EXACT_PATHS = setOf(
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/refresh",
        "/api/auth/logout",
        "/api/cities"
    )

    private val PUBLIC_PATH_PREFIXES = setOf(
        "/api/cities/"
    )

    fun isPublicPath(path: String): Boolean =
        path in PUBLIC_EXACT_PATHS || PUBLIC_PATH_PREFIXES.any { prefix -> path.startsWith(prefix) }

    fun shouldBypassRefresh(path: String): Boolean = isPublicPath(path)
}
