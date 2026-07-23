package com.mefy.platemate.presentation.features.main.scanner.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.PMTheme
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlin.math.roundToInt

@Composable
internal fun FocusReticule(
    offset: Offset,
    color: Color,
    fadeDuration: Int,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val stroke = PMTheme.stroke
    val alpha = remember(offset) { Animatable(1f) }

    LaunchedEffect(offset) {
        alpha.animateTo(0f, animationSpec = tween(fadeDuration))
    }

    val diameterPx = with(density) { 56.dp.toPx() }
    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    (offset.x - diameterPx / 2f).roundToInt(),
                    (offset.y - diameterPx / 2f).roundToInt()
                )
            }
            .size(56.dp)
            .graphicsLayer { this.alpha = alpha.value }
            .border(stroke.st2, color, CircleShape)
    )
}

@Preview
@Composable
private fun FocusReticulePreview() {
    PlateMateTheme {
        Box(modifier = Modifier.size(200.dp)) {
            FocusReticule(
                offset = Offset(100f, 100f),
                color = PMTheme.colors.primary,
                fadeDuration = 2000
            )
        }
    }
}