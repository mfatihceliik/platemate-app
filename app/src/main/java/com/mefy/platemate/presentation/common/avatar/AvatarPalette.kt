package com.mefy.platemate.presentation.common.avatar

import androidx.compose.ui.graphics.Color

/**
 * Deterministic avatar background/foreground colors derived from a stable id,
 * plus initials extraction. Shared so every avatar across the app looks consistent.
 *
 * Standart avatarlar için [com.mefy.platemate.presentation.components.PMAvatar] kullanın:
 * initials'ı kendisi türetir ve renkleri tema uyumlu `pmColors.avatar*` alanlarından alır.
 * Buradaki [colorsFor] yalnızca ViewModel'de renk hesaplayan eski özel avatar çizimleri
 * (ör. UserProfileHeaderCard) için kalmıştır; light değerleri pmColors ile aynıdır.
 */
object AvatarPalette {

    private val pairs = listOf(
        Color(0xFFEEF2FF) to Color(0xFF4F46E5),
        Color(0xFFECFEFF) to Color(0xFF0891B2),
        Color(0xFFF0FDF4) to Color(0xFF15803D),
        Color(0xFFFFF7ED) to Color(0xFFC2410C)
    )

    fun colorsFor(id: Long): Pair<Color, Color> {
        val index = ((id % pairs.size) + pairs.size) % pairs.size
        return pairs[index.toInt()]
    }

    fun initials(name: String): String =
        name.trim()
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .ifEmpty { "?" }
}
