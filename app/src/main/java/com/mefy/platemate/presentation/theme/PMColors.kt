package com.mefy.platemate.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * PlateMate unified color system.
 *
 * Single source of truth for ALL colors in the app — both Material3 standard
 * colors and app-specific semantic colors. Access via [MaterialTheme.pmColors].
 *
 * Usage pattern in composables:
 * ```
 * val colors = MaterialTheme.pmColors
 * Text(color = colors.primary)
 * Box(Modifier.background(colors.surface))
 * Icon(tint = colors.star)
 * ```
 *
 * Material3 components (Button, Card, TextField etc.) automatically pick up
 * the correct colors through a derived [ColorScheme] — no extra wiring needed.
 */
@Immutable
data class PMColors(

    // ── Core Brand ──────────────────────────────────────────────

    val primary: Color,
    val onPrimary: Color,
    val primaryDark: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val primaryContainerBorder: Color,

    // ── Secondary ───────────────────────────────────────────────

    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    // ── Tertiary ────────────────────────────────────────────────

    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    // ── Background & Surface ────────────────────────────────────

    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceSecondary: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,

    // ── Text ────────────────────────────────────────────────────

    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textLabel: Color,
    val textWhite: Color,

    // ── UI Components ───────────────────────────────────────────

    val searchFieldBg: Color,
    val chipBg: Color,
    val cardBorder: Color,
    val cardShadow: Color,
    val ctaShadow: Color,
    val tabActive: Color,
    val tabInactive: Color,

    // ── Status & Error ──────────────────────────────────────────

    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val success: Color,
    val warning: Color,
    val disabled: Color,

    // ── Outline ─────────────────────────────────────────────────

    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,

    // ── Feedback ────────────────────────────────────────────────

    val star: Color,
    val starEmpty: Color,
    val unreadDot: Color,

    // ── Skeleton / Loading ──────────────────────────────────────

    val skeleton: Color,
    val skeletonSecondary: Color,

    // ── Categories ──────────────────────────────────────────────

    val categoryTealBg: Color,
    val categoryTealFg: Color,
    val categoryTealIcon: Color,
    val categoryIndigoBg: Color,
    val categoryIndigoFg: Color,
    val categoryIndigoIcon: Color,
    val categoryOrangeBg: Color,
    val categoryOrangeFg: Color,
    val categoryOrangeIcon: Color,
    val categoryGreenBg: Color,
    val categoryGreenFg: Color,
    val categoryGreenIcon: Color,

    // ── Avatar ──────────────────────────────────────────────────

    val avatarIndigoBg: Color,
    val avatarIndigoFg: Color,
    val avatarTealBg: Color,
    val avatarTealFg: Color,
    val avatarGreenBg: Color,
    val avatarGreenFg: Color,
    val avatarOrangeBg: Color,
    val avatarOrangeFg: Color,

    // ── Rankings ─────────────────────────────────────────────────

    val rankFirstBg: Color,
    val rankFirstFg: Color,
    val rankOtherBg: Color,
    val rankOtherFg: Color,

    // ── Plate Badge ─────────────────────────────────────────────────
    val plateBadge: Color,

    // ── Icon ─────────────────────────────────────────────────
    val iconDefault: Color,
    val iconStar: Color,
    val iconStarEmpty: Color,
    val iconDanger: Color,
)

// ─────────────────────────────────────────────────────────────────
// Light palette
// ─────────────────────────────────────────────────────────────────

val LightPMColors = PMColors(
    // Core Brand
    primary = Color(0xFF06B6D4),
    onPrimary = Color(0xFFFFFFFF),
    primaryDark = Color(0xFF0E7490),
    primaryContainer = Color(0xFFECFEFF),
    onPrimaryContainer = Color(0xFF0E7490),
    primaryContainerBorder = Color(0xFFCFF4FA),

    // Secondary
    secondary = Color(0xFF0891B2),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF164E63),

    // Tertiary
    tertiary = Color(0xFF6366F1),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEEF2FF),
    onTertiaryContainer = Color(0xFF4338CA),

    // Background & Surface
    background = Color(0xFFF6F8FB),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    surfaceSecondary = Color(0xFFF6F8FB),
    surfaceTint = Color(0xFF06B6D4),
    inverseSurface = Color(0xFF0F172A),
    inverseOnSurface = Color(0xFFF1F5F9),

    // Text
    textPrimary = Color(0xFF14161A),
    textSecondary = Color(0xFF475569),
    textTertiary = Color(0xFF64748B),
    textLabel = Color(0xFF94A3B8),
    textWhite = Color(0xFFF1F5F9),

    // UI Components
    searchFieldBg = Color(0xFFEAEFF4),
    chipBg = Color(0xFFF1F5F9),
    cardBorder = Color(0xFFE9EDF2),
    cardShadow = Color(0x0A0F172A),
    ctaShadow = Color(0x8C06B6D4),
    tabActive = Color(0xFF06B6D4),
    tabInactive = Color(0xFF94A3B8),

    // Status & Error
    error = Color(0xFFEF4444),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEF2F2),
    onErrorContainer = Color(0xFF7F1D1D),
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    disabled = Color(0xFFCBD5E1),

    // Outline
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFE9EDF2),
    scrim = Color(0xFF000000),

    // Feedback
    star = Color(0xFFF59E0B),
    starEmpty = Color(0xFFE2E8F0),
    unreadDot = Color(0xFF06B6D4),

    // Skeleton
    skeleton = Color(0xFFE9EEF3),
    skeletonSecondary = Color(0xFFEFF3F7),

    // Categories
    categoryTealBg = Color(0xFFECFEFF),
    categoryTealFg = Color(0xFF0E7490),
    categoryTealIcon = Color(0xFF06B6D4),
    categoryIndigoBg = Color(0xFFEEF2FF),
    categoryIndigoFg = Color(0xFF4338CA),
    categoryIndigoIcon = Color(0xFF6366F1),
    categoryOrangeBg = Color(0xFFFFF7ED),
    categoryOrangeFg = Color(0xFF9A3412),
    categoryOrangeIcon = Color(0xFFF59E0B),
    categoryGreenBg = Color(0xFFF0FDF4),
    categoryGreenFg = Color(0xFF15803D),
    categoryGreenIcon = Color(0xFF22C55E),

    // Avatar
    avatarIndigoBg = Color(0xFFEEF2FF),
    avatarIndigoFg = Color(0xFF4F46E5),
    avatarTealBg = Color(0xFFECFEFF),
    avatarTealFg = Color(0xFF0891B2),
    avatarGreenBg = Color(0xFFF0FDF4),
    avatarGreenFg = Color(0xFF15803D),
    avatarOrangeBg = Color(0xFFFFF7ED),
    avatarOrangeFg = Color(0xFFC2410C),

    // Rankings
    rankFirstBg = Color(0xFF06B6D4),
    rankFirstFg = Color(0xFFFFFFFF),
    rankOtherBg = Color(0xFFE2E8F0),
    rankOtherFg = Color(0xFF475569),

    // Plate Badge
    plateBadge = Color(0xFF003399),

    // Icon
    iconDefault = Color(0xFF64748B),
    iconStar = Color(0xFFF59E0B),
    iconStarEmpty = Color(0xFFE2E8F0),
    iconDanger = Color(0xFFEF4444),
)

// ─────────────────────────────────────────────────────────────────
// Dark palette
// ─────────────────────────────────────────────────────────────────

val DarkPMColors = PMColors(
    // Core Brand
    primary = Color(0xFF22D3EE),
    onPrimary = Color(0xFF164E63),
    primaryDark = Color(0xFF67E8F9),
    primaryContainer = Color(0xFF164E63),
    onPrimaryContainer = Color(0xFF67E8F9),
    primaryContainerBorder = Color(0xFF155E75),

    // Secondary
    secondary = Color(0xFF67E8F9),
    onSecondary = Color(0xFF164E63),
    secondaryContainer = Color(0xFF155E75),
    onSecondaryContainer = Color(0xFFCFFAFE),

    // Tertiary
    tertiary = Color(0xFFA5B4FC),
    onTertiary = Color(0xFF312E81),
    tertiaryContainer = Color(0xFF3730A3),
    onTertiaryContainer = Color(0xFFE0E7FF),

    // Background & Surface
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceSecondary = Color(0xFF162032),
    surfaceTint = Color(0xFF22D3EE),
    inverseSurface = Color(0xFFF1F5F9),
    inverseOnSurface = Color(0xFF0F172A),

    // Text
    textPrimary = Color(0xFFF1F5F9),
    textSecondary = Color(0xFFCBD5E1),
    textTertiary = Color(0xFF94A3B8),
    textLabel = Color(0xFF64748B),
    textWhite = Color(0xFFF1F5F9),

    // UI Components
    searchFieldBg = Color(0xFF1E293B),
    chipBg = Color(0xFF334155),
    cardBorder = Color(0xFF334155),
    cardShadow = Color(0x33000000),
    ctaShadow = Color(0x5922D3EE),
    tabActive = Color(0xFF22D3EE),
    tabInactive = Color(0xFF64748B),

    // Status & Error
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFECACA),
    success = Color(0xFF34D399),
    warning = Color(0xFFFBBF24),
    disabled = Color(0xFF475569),

    // Outline
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF334155),
    scrim = Color(0xFF000000),

    // Feedback
    star = Color(0xFFFBBF24),
    starEmpty = Color(0xFF475569),
    unreadDot = Color(0xFF22D3EE),

    // Skeleton
    skeleton = Color(0xFF334155),
    skeletonSecondary = Color(0xFF293548),

    // Categories
    categoryTealBg = Color(0xFF164E63),
    categoryTealFg = Color(0xFF67E8F9),
    categoryTealIcon = Color(0xFF22D3EE),
    categoryIndigoBg = Color(0xFF312E81),
    categoryIndigoFg = Color(0xFFA5B4FC),
    categoryIndigoIcon = Color(0xFF818CF8),
    categoryOrangeBg = Color(0xFF78350F),
    categoryOrangeFg = Color(0xFFFBBF24),
    categoryOrangeIcon = Color(0xFFFBBF24),
    categoryGreenBg = Color(0xFF14532D),
    categoryGreenFg = Color(0xFF6EE7B7),
    categoryGreenIcon = Color(0xFF34D399),

    // Avatar
    avatarIndigoBg = Color(0xFF312E81),
    avatarIndigoFg = Color(0xFFA5B4FC),
    avatarTealBg = Color(0xFF164E63),
    avatarTealFg = Color(0xFF67E8F9),
    avatarGreenBg = Color(0xFF14532D),
    avatarGreenFg = Color(0xFF6EE7B7),
    avatarOrangeBg = Color(0xFF78350F),
    avatarOrangeFg = Color(0xFFFBBF24),

    // Rankings
    rankFirstBg = Color(0xFF22D3EE),
    rankFirstFg = Color(0xFF164E63),
    rankOtherBg = Color(0xFF475569),
    rankOtherFg = Color(0xFFCBD5E1),

    // Plate Badge
    plateBadge = Color(0xFF003399),

    // Icon
    iconDefault = Color(0xFF64748B),
    iconStar = Color(0xFFFBBF24),
    iconStarEmpty = Color(0xFF475569),
    iconDanger = Color(0xFFF87171),
)

// ─────────────────────────────────────────────────────────────────
// CompositionLocal & extension
// ─────────────────────────────────────────────────────────────────

val LocalPMColors = compositionLocalOf { LightPMColors }

val MaterialTheme.pmColors: PMColors
    @Composable
    @ReadOnlyComposable
    get() = LocalPMColors.current

// ─────────────────────────────────────────────────────────────────
// Material3 ColorScheme derivation (internal implementation detail)
// ─────────────────────────────────────────────────────────────────

/**
 * Derives a Material3 [ColorScheme] from this [PMColors] instance.
 * This ensures Material3 components (Button, Card, TextField etc.)
 * automatically use the correct colors without any extra wiring.
 */
fun PMColors.toColorScheme(isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceTint = surfaceTint,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceTint = surfaceTint,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
        )
    }
}

// ─────────────────────────────────────────────────────────────────
// Accent color support
// ─────────────────────────────────────────────────────────────────

/**
 * Returns a copy of this [PMColors] with the given [accent] applied as the
 * primary brand color. All primary-derived semantic colors (tabActive,
 * unreadDot, ctaShadow, rankFirstBg etc.) are also updated automatically.
 */
fun PMColors.withAccentColor(accent: Color, isDark: Boolean): PMColors {
    val onAccent = if (accent.luminance() > 0.179f) Color(0xFF1E293B) else Color.White
    val container = if (isDark) {
        lerpColor(accent, Color(0xFF0F172A), 0.65f)
    } else {
        lerpColor(accent, Color.White, 0.82f)
    }
    val onContainer = if (isDark) {
        lerpColor(accent, Color.White, 0.65f)
    } else {
        lerpColor(accent, Color(0xFF0F172A), 0.45f)
    }

    return copy(
        primary = accent,
        onPrimary = onAccent,
        primaryContainer = container,
        onPrimaryContainer = onContainer,
        surfaceTint = accent,
        tabActive = accent,
        unreadDot = accent,
        ctaShadow = accent.copy(alpha = if (isDark) 0.35f else 0.55f),
        rankFirstBg = accent,
        rankFirstFg = onAccent,
    )
}

// ─────────────────────────────────────────────────────────────────
// Internal utilities
// ─────────────────────────────────────────────────────────────────

internal fun lerpColor(a: Color, b: Color, t: Float) = Color(
    red = a.red + (b.red - a.red) * t,
    green = a.green + (b.green - a.green) * t,
    blue = a.blue + (b.blue - a.blue) * t,
    alpha = 1f,
)
