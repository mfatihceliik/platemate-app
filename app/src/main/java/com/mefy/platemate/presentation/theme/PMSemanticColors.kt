package com.mefy.platemate.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PMIconColors(
    val favorite: Color,
    val favoriteInactive: Color,
    val destructive: Color
)

@Immutable
data class PMColors(
    val icon: PMIconColors,
    val primaryDark: Color,
    val primaryContainer: Color,
    val primaryContainerBorder: Color,
    val searchFieldBg: Color,
    val chipBg: Color,
    val cardBorder: Color,
    val cardShadow: Color,
    val ctaShadow: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textLabel: Color,
    val star: Color,
    val starEmpty: Color,
    val success: Color,
    val warning: Color,
    val disabled: Color,
    val skeleton: Color,
    val skeletonSecondary: Color,
    val tabActive: Color,
    val tabInactive: Color,
    val surfaceSecondary: Color,
    val errorContainer: Color,
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
    val rankFirstBg: Color,
    val rankFirstFg: Color,
    val rankOtherBg: Color,
    val rankOtherFg: Color,
    val unreadDot: Color
)

val PMColorsLight = PMColors(
    icon = PMIconColors(
        favorite = PMIconLightFavorite,
        favoriteInactive = PMIconLightFavoriteInactive,
        destructive = PMIconLightDestructive
    ),
    primaryDark = Color(0xFF0E7490),
    primaryContainer = Color(0xFFECFEFF),
    primaryContainerBorder = Color(0xFFCFF4FA),
    searchFieldBg = Color(0xFFEAEFF4),
    chipBg = Color(0xFFF1F5F9),
    cardBorder = Color(0xFFE9EDF2),
    cardShadow = Color(0x0A0F172A),
    ctaShadow = Color(0x8C06B6D4),
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textTertiary = Color(0xFF64748B),
    textLabel = Color(0xFF94A3B8),
    star = Color(0xFFF59E0B),
    starEmpty = Color(0xFFE2E8F0),
    success = Color(0xFF10B981),
    warning = Color(0xFFF59E0B),
    disabled = Color(0xFFCBD5E1),
    skeleton = Color(0xFFE9EEF3),
    skeletonSecondary = Color(0xFFEFF3F7),
    tabActive = Color(0xFF06B6D4),
    tabInactive = Color(0xFF94A3B8),
    surfaceSecondary = Color(0xFFF6F8FB),
    errorContainer = Color(0xFFFEF2F2),
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
    rankFirstBg = Color(0xFF06B6D4),
    rankFirstFg = Color(0xFFFFFFFF),
    rankOtherBg = Color(0xFFE2E8F0),
    rankOtherFg = Color(0xFF475569),
    unreadDot = Color(0xFF06B6D4)
)

val PMColorsDark = PMColors(
    icon = PMIconColors(
        favorite = PMIconDarkFavorite,
        favoriteInactive = PMIconDarkFavoriteInactive,
        destructive = PMIconDarkDestructive
    ),
    primaryDark = Color(0xFF67E8F9),
    primaryContainer = Color(0xFF164E63),
    primaryContainerBorder = Color(0xFF155E75),
    searchFieldBg = Color(0xFF1E293B),
    chipBg = Color(0xFF334155),
    cardBorder = Color(0xFF334155),
    cardShadow = Color(0x33000000),
    ctaShadow = Color(0x5922D3EE),
    textPrimary = Color(0xFFF1F5F9),
    textSecondary = Color(0xFFCBD5E1),
    textTertiary = Color(0xFF94A3B8),
    textLabel = Color(0xFF64748B),
    star = Color(0xFFFBBF24),
    starEmpty = Color(0xFF475569),
    success = Color(0xFF34D399),
    warning = Color(0xFFFBBF24),
    disabled = Color(0xFF475569),
    skeleton = Color(0xFF334155),
    skeletonSecondary = Color(0xFF293548),
    tabActive = Color(0xFF22D3EE),
    tabInactive = Color(0xFF64748B),
    surfaceSecondary = Color(0xFF162032),
    errorContainer = Color(0xFF450A0A),
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
    rankFirstBg = Color(0xFF22D3EE),
    rankFirstFg = Color(0xFF164E63),
    rankOtherBg = Color(0xFF475569),
    rankOtherFg = Color(0xFFCBD5E1),
    unreadDot = Color(0xFF22D3EE)
)

val LocalPMColors = compositionLocalOf { PMColorsLight }

val LocalPMIconColors = compositionLocalOf { PMColorsLight.icon }

val MaterialTheme.pmColors: PMColors
    @Composable
    @ReadOnlyComposable
    get() = LocalPMColors.current

val MaterialTheme.pmIconColors: PMIconColors
    @Composable
    @ReadOnlyComposable
    get() = LocalPMIconColors.current
