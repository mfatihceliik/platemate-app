package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMIconContainer(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = MaterialTheme.pmDimensions.sizing.iconMd,
    containerSize: Dp = MaterialTheme.pmDimensions.sizing.iconContainer,
    tint: Color? = null,
    containerColor: Color = MaterialTheme.pmColors.primaryContainer,
    shape: Shape = RoundedCornerShape(MaterialTheme.pmDimensions.radius.r10),
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
    iconSize: Dp = MaterialTheme.pmDimensions.sizing.iconMd,
    containerSize: Dp = MaterialTheme.pmDimensions.sizing.iconContainer,
    tint: Color = Color.Unspecified,
    containerColor: Color = MaterialTheme.pmColors.primaryContainer,
    shape: Shape = RoundedCornerShape(MaterialTheme.pmDimensions.radius.r10),
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
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {

        PMText(
            text = "Default",
            style = PMTextStyle.SectionLabel,
            color = colors.textTertiary
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
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
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
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
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = dims.sizing.iconContainer,
                iconSize = dims.sizing.iconSm
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = dims.sizing.iconContainer,
                iconSize = dims.sizing.iconMd
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = dims.sizing.iconContainer,
                iconSize = dims.sizing.iconLg
            )

            PMIconContainer(
                imageVector = Icons.Default.Home,
                containerSize = dims.sizing.iconContainer,
                iconSize = dims.sizing.iconXl
            )
        }
    }
}