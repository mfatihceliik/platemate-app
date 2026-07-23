package com.mefy.platemate.presentation.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PMColors(
    // ── Core Brand ──────────────────────────────────────────────
    val primary: Color,
    val onPrimary: Color,
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
    val categoryGreenBg: Color,
    val categoryGreenFg: Color,

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
    val iconWarning: Color
)
val LocalColors = compositionLocalOf<PMColors> {
    error("No colors provided")
}