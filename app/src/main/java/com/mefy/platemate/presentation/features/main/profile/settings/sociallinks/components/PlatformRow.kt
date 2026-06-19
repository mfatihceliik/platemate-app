package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PlatformRow(
    platform: String,
    isConnected: Boolean,
    displayValue: String?,
    onConnectClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val platformInfo = getPlatformInfo(platform)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.platformRowIcon)
                .clip(MaterialTheme.shapes.small)
                .background(platformInfo.bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = platformInfo.iconRes),
                contentDescription = platformInfo.displayName,
                tint = platformInfo.iconTint,
                modifier = Modifier.size(dims.sizing.iconLg)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            PMText(
                text = platformInfo.displayName,
                style = PMTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
            PMText(
                text = if (isConnected) displayValue ?: "" else "Bağlı değil",
                style = PMTextStyle.Note,
                color = if (isConnected) colors.primary else MaterialTheme.pmColors.textLabel
            )
        }

        if (isConnected) {
            PMText(
                text = "Kaldır",
                style = PMTextStyle.Caption,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.pmColors.textLabel,
                modifier = Modifier.debouncedClickable(onClick = onRemoveClick)
            )
        } else {
            PMText(
                text = "Bağla",
                style = PMTextStyle.Label,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.pmColors.primaryDark,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(colors.primaryContainer)
                    .padding(horizontal = dims.spacing.s12, vertical = 6.dp)
                    .debouncedClickable(onClick = onConnectClick)
            )
        }
    }
}
