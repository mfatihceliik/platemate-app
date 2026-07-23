package com.mefy.platemate.presentation.features.main.messages.chatdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun QuickAction(
    label: String,
    icon: ImageVector,
    bg: Color,
    tint: Color,
    onClick: () -> Unit
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        PMIconButton(
            imageVector = icon,
            contentDescription = label,
            iconColor = tint,
            variant = PMIconButtonVariant.Filled,
            containerColor = bg,
            size = sizing.iconLg,
            onClick = onClick
        )

        PMText(text = label, style = PMTextStyle.Note, color = colors.textLabel)
    }
}