package com.mefy.platemate.presentation.features.main.messages.chatdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ChatDetailSettingsCard(
    notificationsEnabled: Boolean,
    onNotificationsToggled: () -> Unit,
    onMediaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = dims.stroke.st2, shape = RoundedCornerShape(dims.radius.r18))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(dims.radius.r18))
            .border(dims.stroke.st1, colors.cardBorder, RoundedCornerShape(dims.radius.r18))
    ) {
        NotificationsRow(
            enabled = notificationsEnabled,
            onToggle = onNotificationsToggled,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.cardBorder, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.PhotoLibrary,
            label = stringResource(R.string.chatdetail_media_files),
            iconBg = MaterialTheme.pmColors.primaryContainer,
            iconTint = MaterialTheme.colorScheme.primary,
            onClick = onMediaClick,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.cardBorder, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.DeleteOutline,
            label = stringResource(R.string.chatdetail_delete_chat),
            iconBg = MaterialTheme.colorScheme.errorContainer,
            iconTint = MaterialTheme.colorScheme.error,
            labelColor = MaterialTheme.colorScheme.error,
            onClick = onDeleteClick,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.cardBorder, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.Flag,
            label = stringResource(R.string.chatdetail_report),
            iconBg = colors.chipBg,
            iconTint = colors.textSecondary,
            onClick = onReportClick,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )
    }
}

@Composable
private fun NotificationsRow(
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(dims.sizing.settingsRowIcon)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(dims.radius.r10))
                    .padding(dims.spacing.s8)
            )
            PMText(
                text = stringResource(R.string.chatdetail_notifications),
                style = PMTextStyle.Body,
                color = colors.textPrimary
            )
        }
        PMSwitch(
            checked = enabled,
            onCheckedChange = { onToggle() }
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.pmColors.textPrimary
) {
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .debouncedClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier
                .size(dims.sizing.settingsRowIcon)
                .background(iconBg, RoundedCornerShape(dims.radius.r10))
                .padding(dims.spacing.s8)
        )
        PMText(
            text = label,
            style = PMTextStyle.Body,
            color = labelColor
        )
    }
}
