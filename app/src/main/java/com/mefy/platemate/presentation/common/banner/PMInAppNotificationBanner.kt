package com.mefy.platemate.presentation.common.banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.theme.PMTheme
@Composable
fun PMInAppNotificationBanner(
    modifier: Modifier = Modifier,
    banner: InAppBannerUiModel?,
    resetKey: Int,
    onDismiss: () -> Unit
) {
    val animationSpeed = PMTheme.animations
    var shown by remember { mutableStateOf<InAppBannerUiModel?>(null) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(resetKey) {
        if (banner != null) {
            shown = banner
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(animationSpeed.longDuration5, easing = LinearEasing)
            )
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = banner != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
    ) {
        shown?.let { current ->
            PMBannerCard(
                banner = current,
                progress = progress.value,
                onDismiss = onDismiss
            )
        }
    }
}


