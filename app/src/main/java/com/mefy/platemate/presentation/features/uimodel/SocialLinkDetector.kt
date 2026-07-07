package com.mefy.platemate.presentation.features.uimodel

import java.net.URI

/**
 * Auto-detects which social platform a user-pasted profile URL belongs to by matching the URL's
 * host against each platform's [SocialPlatform.baseUrl] (served by the backend catalog).
 *
 * Returns null when nothing matches — the caller treats that as an unsupported link.
 */
fun detectPlatform(rawUrl: String, platforms: List<SocialPlatform>): SocialPlatform? {
    val host = hostOf(rawUrl) ?: return null

    platforms.firstOrNull { platform ->
        val baseHost = platform.baseUrl?.let(::hostOf) ?: return@firstOrNull false
        host == baseHost || host.endsWith(".$baseHost") || baseHost.endsWith(".$host")
    }?.let { return it }

    // Fallback: platform code appears as a host label (e.g. "github" in github.io).
    // Guarded to codes >= 4 chars so single-letter codes like "X" never over-match.
    return platforms.firstOrNull { platform ->
        platform.code.length >= 4 && host.split('.').any { it == platform.code.lowercase() }
    }
}

/**
 * Extracts a normalized host (lowercase, no leading "www.") from a raw URL that may omit its scheme.
 */
private fun hostOf(raw: String): String? {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return null
    val withScheme = if (trimmed.contains("://")) trimmed else "https://$trimmed"
    val host = runCatching { URI(withScheme).host }.getOrNull()?.lowercase() ?: return null
    return host.removePrefix("www.")
}
