package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMSizing(

    /* TOPBAR */
    val topBarHeight: Dp = 48.dp,

    /* POPUP */
    val popupSize: Dp = 320.dp,

    /* ICON */
    val iconXs: Dp = 16.dp,
    val iconSm: Dp = 20.dp,
    val iconMd: Dp = 24.dp,
    val iconLg: Dp = 32.dp,
    val iconXl: Dp = 48.dp,
    val iconContainer: Dp = 36.dp,

    /* AVATAR */
    val avatarXs: Dp = 24.dp,
    val avatarSm: Dp = 32.dp,
    val avatarMd: Dp = 48.dp,
    val avatarLg: Dp = 64.dp,
    val avatarXl: Dp = 96.dp,

    /* CIRCLE PROGRESSBAR */
    val circleProgressBarXs: Dp = 24.dp,
    val circleProgressBarSm: Dp = 32.dp,
    val circleProgressBarMd: Dp = 48.dp,
    val circleProgressBarLg: Dp = 64.dp,


    /* RATING BAR */
    val ratingBarHeight: Dp = 12.dp,

    /* COMMENT FIELD */
    val commentFieldHeight: Dp = 150.dp,

    /* BOTTOM BAR */
    val bottomBarHeight: Dp = 96.dp,
    val bottomBarContentHeight: Dp = 72.dp,


    /* PLATE BADGE */
    val plateBadgeXs: Dp = 24.dp,
    val plateBadgeSm: Dp = 32.dp,
    val plateBadgeMd: Dp = 48.dp,
    val plateBadgeLg: Dp = 64.dp,
    val plateBadgeXl: Dp = 96.dp,

    /* PROFILE HEADER */
    val profileHeaderSize: Dp = 96.dp,
    val profileHeaderGradientHeight: Dp = 64.dp,

    /* BUTTON */
    val buttonMinHeight: Dp = 48.dp,

    /* TEXT FIELD */
    val textFieldMinHeight: Dp = 52.dp,

    /* SEARCHBAR */
    val searchBarHeight: Dp = 52.dp,

    /* CHAT */


    val gridHeight: Dp = 144.dp,

    val chipHeight: Dp = 34.dp,
    val rankBadgeSize: Dp = 28.dp,
    val savedPlateCardWidth: Dp = 150.dp,
    val categoryIconContainer: Dp = 36.dp,
    val categoryIconDot: Dp = 12.dp,
    val stripeWidthLarge: Dp = 8.dp,


    // Chat composer: circular +/send buttons and the pill input's min height.
    val chatComposerButton: Dp = 40.dp,
    val chatFieldMinHeight: Dp = 42.dp,
)