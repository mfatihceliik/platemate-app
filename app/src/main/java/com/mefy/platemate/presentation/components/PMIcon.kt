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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun PMIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    size: Dp = MaterialTheme.pmDimensions.sizing.iconMd,
    tint: Color? = null,
    containerColor: Color? = null
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    if (containerColor != null) {
        Box(
            modifier = Modifier
                .size(dims.sizing.settingsRowIcon)
                .clip(RoundedCornerShape(dims.radius.r10))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                tint = tint ?: colors.primary,
                contentDescription = contentDescription
            )
        }
    } else {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint ?: colors.primary,
            modifier = modifier.size(size),
        )
    }
}

@Composable
fun PMIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    size: Dp = MaterialTheme.pmDimensions.sizing.iconMd,
    tint: Color = Color.Unspecified,
    containerColor: Color? = null
) {
    val dims = MaterialTheme.pmDimensions

    if (containerColor != null) {
        Box(
            modifier = Modifier
                .size(dims.sizing.settingsRowIcon)
                .clip(RoundedCornerShape(dims.radius.r10))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                tint = tint,
                contentDescription = contentDescription
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = tint,
            modifier = modifier.size(size),
        )
    }
}

@Preview(name = "PMIcon Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMIconLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMIconPreviewContent()
    }
}

@Preview(name = "PMIcon Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMIconDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMIconPreviewContent()
    }
}

@Composable
private fun PMIconPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s24),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIcon(imageVector = Icons.Filled.Home, contentDescription = null, size = dims.sizing.iconSm, tint = colors.onBackground)
            PMIcon(imageVector = Icons.Filled.Home, contentDescription = null, size = dims.sizing.iconMd, tint = colors.onBackground)
            PMIcon(imageVector = Icons.Filled.Home, contentDescription = null, size = dims.sizing.iconLg, tint = colors.onBackground)
            PMIcon(imageVector = Icons.Filled.Home, contentDescription = null, size = dims.sizing.iconXl, tint = colors.onBackground)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s24),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMIcon(imageVector = Icons.Filled.Search, contentDescription = null, tint = colors.primary)
            PMIcon(imageVector = Icons.Filled.Favorite, contentDescription = null, tint = colors.error)
            PMIcon(imageVector = Icons.Filled.Settings, contentDescription = null, tint = colors.secondary)
        }
    }
}
