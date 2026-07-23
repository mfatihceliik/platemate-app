package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMIconContainer(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = PMTheme.sizing.iconMd,
    containerSize: Dp = PMTheme.sizing.iconContainer,
    tint: Color? = null,
    containerColor: Color = PMTheme.colors.primaryContainer,
    shape: Shape = PMTheme.shapes.medium,
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        PMIcon(
            imageVector = imageVector,
            size = iconSize,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun PMIconContainer(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = PMTheme.sizing.iconMd,
    containerSize: Dp = PMTheme.sizing.iconContainer,
    tint: Color = Color.Unspecified,
    containerColor: Color = PMTheme.colors.primaryContainer,
    shape: Shape = PMTheme.shapes.medium,
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        PMIcon(
            painter = painter,
            size = iconSize,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Preview(name = "PMIconContainer Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMIconContainerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMIconContainerPreviewContent()
    }
}

@Preview(name = "PMIconContainer Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMIconContainerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMIconContainerPreviewContent()
    }
}

@Composable
private fun PMIconContainerPreviewContent() {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(spacing.s24),
        verticalArrangement = Arrangement.spacedBy(spacing.s24)
    ) {

        PMText(
            text = "Default",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIconContainer(
                imageVector = Icons.Default.Home
            )

            PMIconContainer(
                imageVector = Icons.Default.Search
            )

            PMIconContainer(
                imageVector = Icons.Default.Settings
            )

            PMIconContainer(
                imageVector = Icons.Default.Favorite
            )
        }

        PMText(
            text = "Custom Colors",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerColor = colors.primaryContainer,
                tint = colors.onPrimaryContainer
            )

            PMIconContainer(
                imageVector = Icons.Default.Favorite,
                containerColor = colors.errorContainer,
                tint = colors.onErrorContainer
            )

            PMIconContainer(
                imageVector = Icons.Default.Settings,
                containerColor = colors.secondaryContainer,
                tint = colors.onSecondaryContainer
            )

            PMIconContainer(
                imageVector = Icons.Default.Search,
                containerColor = colors.surfaceVariant,
                tint = colors.onSurfaceVariant
            )
        }

        PMText(
            text = "Sizes",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = sizing.iconContainer,
                iconSize = sizing.iconSm
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = sizing.iconContainer,
                iconSize = sizing.iconMd
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = sizing.iconContainer,
                iconSize = sizing.iconLg
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = sizing.iconContainer,
                iconSize = sizing.iconXl
            )
        }
    }
}