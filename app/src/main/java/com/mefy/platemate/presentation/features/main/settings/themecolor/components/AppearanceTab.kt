package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun AppearanceTab(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val stroke = PMTheme.stroke
    val radius = PMTheme.radius
    val sizing = PMTheme.sizing

    // Instant color change instead of animation to prevent flickering when theme changes globally.
    val containerColor = if (isSelected) colors.surface else Color.Transparent
    val contentColor = if (isSelected) colors.primary else colors.textSecondary
    val elevation = if (isSelected) stroke.st2 else spacing.s0

    val shape = RoundedCornerShape(radius.r10)
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .height(spacing.s48)
            .shadow(elevation = elevation, shape = shape, clip = false)
            .clip(shape)
            .background(containerColor)
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PMIcon(
            imageVector = icon,
            tint = contentColor,
            size = sizing.iconMd
        )
        PMText(
            text = label,
            style = PMTextStyle.Caption,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor,
            modifier = Modifier.padding(top = spacing.s4)
        )
    }
}
