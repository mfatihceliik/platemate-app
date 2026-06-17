package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SettingsNavRow(
    iconBg: Color,
    iconContent: @Composable () -> Unit,
    label: String,
    badge: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.settingsRowIcon)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            iconContent()
        }

        PMText(
            text = label,
            style = PMTextStyle.Body,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        if (badge != null) {
            PMText(
                text = badge,
                style = PMTextStyle.SectionLabel,
                color = Color.White,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(colors.star)
                    .padding(horizontal = dims.spacing.s10, vertical = dims.spacing.s4)
            )
        }

        trailing?.invoke()

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(dims.sizing.iconMd)
        )
    }
}
