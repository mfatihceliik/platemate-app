package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMButtonStyle
import com.mefy.platemate.presentation.components.model.PMTextStyle

@Composable
internal fun SplashContent(
    onStartClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0B0F17),
                        Color(0xFF071921),
                        Color(0xFF0B0F17)
                    )
                )
            )
    ) {
        val colors = MaterialTheme.pmColors

        // Glow orbs
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-60).dp)
                .size(320.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.22f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = (-180).dp)
                .size(200.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            colors.success.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dims.spacing.s48, bottom = dims.spacing.s48, start = dims.spacing.s24, end = dims.spacing.s24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top half: Animations and Logo
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingPlatesAnimation()
                Spacer(modifier = Modifier.height(dims.spacing.s32))
                
                // Logo Wordmark
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(
                                elevation = 28.dp,
                                spotColor = colors.primary.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(dims.radius.r16)
                            )
                            .background(colors.primary, RoundedCornerShape(dims.radius.r16)),
                        contentAlignment = Alignment.Center
                    ) {
                        PMText(
                            text = "P",
                            style = PMTextStyle.Display,
                            color = Color.White
                        )
                    }
                    PMText(
                        text = "PlateMate",
                        style = PMTextStyle.Display,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(dims.spacing.s8))
                PMText(
                    text = stringResource(R.string.onboarding_splash_desc),
                    style = PMTextStyle.Body,
                    color = colors.textLabel.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }

            // CTA Area
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s12)
            ) {
                PMButton(
                    text = stringResource(R.string.onboarding_splash_start),
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth(),
                    style = PMButtonStyle.Filled
                )

                PMButton(
                    text = stringResource(R.string.onboarding_splash_login),
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    style = PMButtonStyle.Outlined
                )
            }
        }
    }
}


