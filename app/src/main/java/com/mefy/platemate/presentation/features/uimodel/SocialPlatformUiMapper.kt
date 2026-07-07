package com.mefy.platemate.presentation.features.uimodel

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.social.SocialPlatform as DomainSocialPlatform

val SocialPlatformFallbackBg = Color(0xFFF1F5F9)
val SocialPlatformFallbackTint = Color(0xFF64748B)

fun DomainSocialPlatform.toUiModel(): SocialPlatform = SocialPlatform(
    id = id,
    code = code,
    displayName = label,
    iconUrl = iconUrl,
    baseUrl = baseUrl,
    backgroundColor = parseHexColor(backgroundColorHex, SocialPlatformFallbackBg),
    iconTint = parseHexColor(iconTintColorHex, SocialPlatformFallbackTint)
)

private fun parseHexColor(hex: String?, fallback: Color): Color {
    if (hex.isNullOrBlank()) return fallback
    return runCatching { Color(parseColor(hex)) }.getOrDefault(fallback)
}
