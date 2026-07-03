package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AppearanceTab(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Smooth toggle: animate the selected pill's surface, content tint and lift.
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colors.surface else Color.Transparent,
        label = "appearanceTabContainer"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.textSecondary,
        label = "appearanceTabContent"
    )
    val elevation by animateDpAsState(
        targetValue = if (isSelected) dims.stroke.st2 else dims.spacing.s0,
        label = "appearanceTabElevation"
    )

    val shape = RoundedCornerShape(dims.radius.r10)

    Column(
        modifier = modifier
            .height(dims.spacing.s48)
            .shadow(elevation = elevation, shape = shape, clip = false)
            .clip(shape)
            .background(containerColor)
            .debouncedClickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PMIcon(
            imageVector = icon,
            tint = contentColor,
            size = dims.sizing.iconMd
        )
        PMText(
            text = label,
            style = PMTextStyle.Caption,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor,
            modifier = Modifier.padding(top = dims.spacing.s4)
        )
    }
}
