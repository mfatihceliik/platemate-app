package com.mefy.platemate.presentation.features.auth.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun WelcomeScreen(
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius
    val stroke = dimensions.stroke
    val backgroundBrush = rememberWelcomeBackgroundBrush()

    val badgeWidth = spacing.s32 * 4 + spacing.s22
    val badgeHeight = spacing.s36
    val badgeContainerHeight = spacing.s56
    val featureRowSpacing = spacing.s10 + spacing.s2

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.s22, vertical = spacing.s16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s18)
    ) {
        PlateBadge(
            plateText = stringResource(R.string.welcome_plate_badge),
            width = badgeWidth,
            height = badgeHeight,
            containerHeight = badgeContainerHeight,
            modifier = Modifier.padding(top = spacing.s18, bottom = spacing.s8)
        )

        PMText(
            text = stringResource(R.string.welcome_title),
            style = PMTextStyle.Display,
            color = colorScheme.onBackground
        )
        PMText(
            text = stringResource(R.string.welcome_subtitle),
            style = PMTextStyle.Body,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        FeatureCard(
            features = WelcomeFeatures,
            modifier = Modifier.fillMaxWidth(),
            featureRowSpacing = featureRowSpacing
        )

        Button(
            onClick = onCreateAccountClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.s52),
            shape = RoundedCornerShape(radius.r14),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = stringResource(R.string.welcome_create_account),
                    style = PMTextStyle.Label,
                    color = colorScheme.onPrimary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = colorScheme.onPrimary,
                    modifier = Modifier.size(spacing.s20)
                )
            }
        }

        OutlinedButton(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(spacing.s52),
            shape = RoundedCornerShape(radius.r14),
            border = BorderStroke(stroke.st1, colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colorScheme.onSurface
            )
        ) {
            PMText(
                text = stringResource(R.string.welcome_sign_in),
                style = PMTextStyle.Label,
                color = colorScheme.onSurface
            )
        }

        PMText(
            text = stringResource(R.string.welcome_terms),
            style = PMTextStyle.Caption,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = spacing.s8, vertical = spacing.s6)
        )
    }
}

@Composable
private fun PlateBadge(
    plateText: String,
    width: Dp,
    height: Dp,
    containerHeight: Dp,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val radius = dimensions.radius

    Box(
        modifier = modifier.size(width = width, height = containerHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = width - dimensions.spacing.s4, height = height)
                .rotate(4f)
                .background(
                    color = colorScheme.secondaryContainer.copy(alpha = 0.48f),
                    shape = RoundedCornerShape(radius.r10)
                )
                .border(
                    width = dimensions.stroke.st1,
                    color = colorScheme.outline.copy(alpha = 0.38f),
                    shape = RoundedCornerShape(radius.r10)
                )
        )

        Box(
            modifier = Modifier
                .size(width = width - dimensions.spacing.s4, height = height)
                .rotate(-4f)
                .background(
                    color = colorScheme.secondaryContainer.copy(alpha = 0.30f),
                    shape = RoundedCornerShape(radius.r10)
                )
                .border(
                    width = dimensions.stroke.st1,
                    color = colorScheme.outline.copy(alpha = 0.30f),
                    shape = RoundedCornerShape(radius.r10)
                )
        )

        Box(
            modifier = Modifier
                .size(width = width - dimensions.spacing.s4, height = height)
                .background(
                    color = colorScheme.primaryContainer,
                    shape = RoundedCornerShape(radius.r10)
                )
                .border(
                    width = dimensions.stroke.st1,
                    color = colorScheme.outline,
                    shape = RoundedCornerShape(radius.r10)
                ),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = plateText,
                style = PMTextStyle.Title,
                color = colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun FeatureCard(
    features: List<WelcomeFeatureItem>,
    featureRowSpacing: Dp,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius

    Column(
        modifier = modifier
            .background(
                color = colorScheme.surfaceVariant.copy(alpha = 0.94f),
                shape = RoundedCornerShape(radius.r18)
            )
            .border(
                width = dimensions.stroke.st1,
                color = colorScheme.outlineVariant,
                shape = RoundedCornerShape(radius.r18)
            )
            .padding(horizontal = spacing.s14, vertical = spacing.s14),
        verticalArrangement = Arrangement.spacedBy(featureRowSpacing)
    ) {
        features.forEach { feature ->
            FeatureRow(feature = feature)
        }
    }
}

@Composable
private fun FeatureRow(feature: WelcomeFeatureItem, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(spacing.s10 + spacing.s2)
    ) {
        Box(
            modifier = Modifier
                .size(spacing.s36)
                .background(
                    color = colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(radius.r10)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = colorScheme.tertiary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(spacing.s2)) {
            PMText(
                text = stringResource(feature.titleRes),
                style = PMTextStyle.Title,
                color = colorScheme.onSurface
            )
            PMText(
                text = stringResource(feature.descriptionRes),
                style = PMTextStyle.Caption,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun rememberWelcomeBackgroundBrush(): Brush {
    val colorScheme = MaterialTheme.colorScheme
    return remember(
        colorScheme.background,
        colorScheme.surface,
        colorScheme.surfaceVariant
    ) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.background,
                colorScheme.surface,
                colorScheme.surfaceVariant.copy(alpha = 0.82f)
            )
        )
    }
}

@Immutable
private data class WelcomeFeatureItem(
    val icon: ImageVector,
    val titleRes: Int,
    val descriptionRes: Int
)

private val WelcomeFeatures = listOf(
    WelcomeFeatureItem(
        icon = Icons.Filled.Search,
        titleRes = R.string.welcome_feature_plate_title,
        descriptionRes = R.string.welcome_feature_plate_desc
    ),
    WelcomeFeatureItem(
        icon = Icons.Filled.Star,
        titleRes = R.string.welcome_feature_review_title,
        descriptionRes = R.string.welcome_feature_review_desc
    ),
    WelcomeFeatureItem(
        icon = Icons.Filled.Build,
        titleRes = R.string.welcome_feature_claim_title,
        descriptionRes = R.string.welcome_feature_claim_desc
    ),
    WelcomeFeatureItem(
        icon = Icons.Filled.Call,
        titleRes = R.string.welcome_feature_chat_title,
        descriptionRes = R.string.welcome_feature_chat_desc
    )
)

@Preview(
    name = "Welcome Dark",
    showBackground = true,
    backgroundColor = 0xFF07153A
)
@Composable
private fun WelcomeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        WelcomeScreen(
            onCreateAccountClick = {},
            onSignInClick = {}
        )
    }
}

@Preview(
    name = "Welcome Light",
    showBackground = true,
    backgroundColor = 0xFFF3F6FF
)
@Composable
private fun WelcomeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        WelcomeScreen(
            onCreateAccountClick = {},
            onSignInClick = {}
        )
    }
}

@Preview(
    name = "Welcome Compact",
    widthDp = 360,
    heightDp = 640,
    showBackground = true,
    backgroundColor = 0xFF07153A
)
@Composable
private fun WelcomeCompactPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        WelcomeScreen(
            onCreateAccountClick = {},
            onSignInClick = {}
        )
    }
}
