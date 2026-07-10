package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun FloatingPlatesAnimation() {
    val transition = rememberInfiniteTransition(label = "floating")

    val floatA by transition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatA"
    )

    val floatB by transition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatB"
    )

    val floatC by transition.animateFloat(
        initialValue = -4f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatC"
    )

    val shimmer by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Box(
        modifier = Modifier
            .width(260.dp)
            .height(220.dp)
    ) {
        // Plate 3 (Bottom left)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 28.dp, y = (-8).dp + floatC.dp)
                .size(width = 110.dp, height = 46.dp)
                .shadow(20.dp, spotColor = colors.primary.copy(alpha = 0.18f))
                .background(Color(0xFF1E2A36), RoundedCornerShape(dims.radius.r12))
                .border(dims.stroke.st1, colors.primary.copy(alpha = 0.35f), RoundedCornerShape(dims.radius.r12))
                .clip(RoundedCornerShape(dims.radius.r12)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(8.dp).fillMaxSize().background(colors.primary.copy(alpha = 0.6f)).align(Alignment.CenterStart))
            PMText(text = "35 MK 777", style = PMTextStyle.Label, color = colors.textLabel, modifier = Modifier.padding(start = dims.spacing.s8))
        }

        // Plate 2 (Right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-8).dp, y = 80.dp + floatB.dp)
                .rotate(3f)
                .size(width = 120.dp, height = 52.dp)
                .shadow(28.dp, spotColor = colors.textPrimary.copy(alpha = 0.35f))
                .background(Color.White, RoundedCornerShape(dims.radius.r12))
                .clip(RoundedCornerShape(dims.radius.r12)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(9.dp).fillMaxSize().background(colors.primary).align(Alignment.CenterStart))
            PMText(text = "06 AB 1234", style = PMTextStyle.Title, color = colors.primaryDark, modifier = Modifier.padding(start = dims.spacing.s8))
        }

        // Plate 1 (Top left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 10.dp + floatA.dp)
                .rotate(-4f)
                .size(width = 120.dp, height = 52.dp)
                .shadow(28.dp, spotColor = colors.primary.copy(alpha = 0.45f))
                .background(colors.primary, RoundedCornerShape(dims.radius.r12))
                .clip(RoundedCornerShape(dims.radius.r12)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.width(9.dp).fillMaxSize().background(colors.primaryDark).align(Alignment.CenterStart))
            PMText(text = "34 EK 0682", style = PMTextStyle.Title, color = Color.White, modifier = Modifier.padding(start = dims.spacing.s8))
        }

        // Stars
        PMIcon(
            imageVector = Icons.Filled.Star,
            tint = colors.iconStar,
            size = dims.sizing.iconLg,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 0.dp, y = 0.dp)
                .alpha(shimmer)
        )

        PMIcon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = colors.iconStar.copy(alpha = 0.7f),
            size = dims.sizing.iconMd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-28).dp, y = (-20).dp)
                .alpha(shimmer)
        )

        PMIcon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = colors.primary.copy(alpha = 0.8f),
            size = dims.sizing.iconSm,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 0.dp, y = 20.dp)
                .alpha(shimmer)
        )
    }
}