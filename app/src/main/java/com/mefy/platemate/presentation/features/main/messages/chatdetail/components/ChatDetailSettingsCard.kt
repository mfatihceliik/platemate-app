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
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
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
            .background(colors.surface, RoundedCornerShape(dims.radius.r18))
            .border(dims.stroke.st1, colors.outlineVariant, RoundedCornerShape(dims.radius.r18))
    ) {
        NotificationsRow(
            enabled = notificationsEnabled,
            onToggle = onNotificationsToggled,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.outlineVariant, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.PhotoLibrary,
            label = stringResource(R.string.chatdetail_media_files),
            iconBg = colors.primaryContainer,
            iconTint = colors.primary,
            onClick = onMediaClick,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.outlineVariant, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.DeleteOutline,
            label = stringResource(R.string.chatdetail_delete_chat),
            iconBg = colors.errorContainer,
            iconTint = colors.error,
            labelColor = colors.error,
            onClick = onDeleteClick,
            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
        )

        HorizontalDivider(color = colors.outlineVariant, thickness = dims.stroke.st1)

        SettingsRow(
            icon = Icons.Outlined.Flag,
            label = stringResource(R.string.chatdetail_report),
            iconBg = colors.surfaceVariant,
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
            PMIcon(
                imageVector = Icons.Outlined.Notifications,
                tint = colors.secondary,
                size = dims.sizing.avatarIconInner,
                modifier = Modifier
                    .background(colors.secondaryContainer, RoundedCornerShape(dims.radius.r10))
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
        PMIcon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            size = dims.sizing.avatarIconInner,
            modifier = Modifier
                .background(iconBg, RoundedCornerShape(dims.radius.r10))
        )
        PMText(
            text = label,
            style = PMTextStyle.Body,
            color = labelColor
        )
    }
}

@Preview(name = "ChatDetailSettingsCard Light", showBackground = true, backgroundColor = 0xFFF1F5F9)
@Composable
private fun ChatDetailSettingsCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ChatDetailSettingsCard(
            notificationsEnabled = true,
            onNotificationsToggled = {},
            onMediaClick = {},
            onDeleteClick = {},
            onReportClick = {}
        )
    }
}

@Preview(name = "ChatDetailSettingsCard Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ChatDetailSettingsCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ChatDetailSettingsCard(
            notificationsEnabled = false,
            onNotificationsToggled = {},
            onMediaClick = {},
            onDeleteClick = {},
            onReportClick = {}
        )
    }
}
