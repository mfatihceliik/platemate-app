package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMSizing(
    val avatarSmall: Dp = 36.dp,
    val avatarMedium: Dp = 48.dp,
    val avatarLarge: Dp = 80.dp,
    val avatarXLarge: Dp = 72.dp,
    val avatarHero: Dp = 90.dp,
    val avatarIconInner: Dp = 40.dp,
    val mainBottomBarHeight: Dp = 88.dp,
    val mainBottomBarContentHeight: Dp = 64.dp,
    val progressBarSmall: Dp = 24.dp,
    val progressBarMedium: Dp = 48.dp,
    val plateBadgeSmall: Dp = 40.dp,
    val plateBadgeMedium: Dp = 44.dp,
    val plateBadgeLarge: Dp = 64.dp,
    val plateBadgeReview: Dp = 52.dp,
    val searchBarHeight: Dp = 44.dp,
    val topBarHeight: Dp = 56.dp,
    val ctaHeight: Dp = 48.dp,
    val ctaHeightLarge: Dp = 52.dp,
    val chipHeight: Dp = 34.dp,
    val rankBadgeSize: Dp = 28.dp,
    val unreadDotSize: Dp = 8.dp,
    val savedPlateCardWidth: Dp = 150.dp,
    val categoryIconContainer: Dp = 36.dp,
    val categoryIconDot: Dp = 12.dp,
    val ratingBarHeight: Dp = 8.dp,
    val stripeWidthSmall: Dp = 6.dp,
    val stripeWidthLarge: Dp = 8.dp,
    val stripeWidthReview: Dp = 8.dp,
    val iconSm: Dp = 16.dp,
    val iconMd: Dp = 20.dp,
    val iconLg: Dp = 24.dp,
    val iconXl: Dp = 28.dp,
    val iconHuge: Dp = 32.dp,
    val settingsRowIcon: Dp = 36.dp,
    val platformRowIcon: Dp = 40.dp,
)