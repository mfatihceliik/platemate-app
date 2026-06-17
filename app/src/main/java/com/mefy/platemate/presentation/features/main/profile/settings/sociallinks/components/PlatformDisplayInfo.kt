package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.components

import androidx.compose.ui.graphics.Color
import com.mefy.platemate.R

internal data class PlatformDisplayInfo(
    val displayName: String,
    val iconRes: Int,
    val bgColor: Color,
    val iconTint: Color
)

internal fun getPlatformInfo(platform: String): PlatformDisplayInfo {
    return when (platform.uppercase()) {
        "INSTAGRAM" -> PlatformDisplayInfo(
            displayName = "Instagram",
            iconRes = R.drawable.ic_instagram,
            bgColor = Color(0xFFFDF2F8),
            iconTint = Color(0xFFDB2777)
        )
        "X", "TWITTER" -> PlatformDisplayInfo(
            displayName = "X (Twitter)",
            iconRes = R.drawable.ic_x,
            bgColor = Color(0xFFF1F5F9),
            iconTint = Color(0xFF0F172A)
        )
        "FACEBOOK" -> PlatformDisplayInfo(
            displayName = "Facebook",
            iconRes = R.drawable.ic_facebook,
            bgColor = Color(0xFFEFF6FF),
            iconTint = Color(0xFF2563EB)
        )
        else -> PlatformDisplayInfo(
            displayName = platform,
            iconRes = R.drawable.ic_link,
            bgColor = Color(0xFFF1F5F9),
            iconTint = Color(0xFF64748B)
        )
    }
}
