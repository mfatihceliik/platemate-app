package com.mefy.platemate.presentation.features.main.messages.chatdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.material3.Icon
import com.mefy.platemate.presentation.components.model.PMTextStyle


@Composable
internal fun ChatDetailUserCard(
    participantName: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    onMessageClick: () -> Unit,
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
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(modifier = Modifier.size(dims.sizing.avatarXLarge)) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarXLarge)
                    .clip(CircleShape)
                    .background(avatarBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = initials,
                    style = PMTextStyle.Headline,
                    fontWeight = FontWeight.Bold,
                    color = avatarFg
                )
            }
        }

        PMText(
            text = participantName,
            style = PMTextStyle.Title,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16)) {
            QuickAction(
                label = "Mesaj",
                icon = Icons.Outlined.ChatBubbleOutline,
                bg = colors.primaryContainer,
                tint = MaterialTheme.colorScheme.primary,
                onClick = onMessageClick
            )
            QuickAction(
                label = "Plaka",
                icon = Icons.Outlined.DirectionsCar,
                bg = colors.categoryGreenBg,
                tint = colors.categoryGreenFg,
                onClick = {}
            )
            QuickAction(
                label = "Engelle",
                icon = Icons.Outlined.Block,
                bg = MaterialTheme.colorScheme.errorContainer,
                tint = MaterialTheme.colorScheme.error,
                onClick = {}
            )
        }
    }
}

@Composable
private fun QuickAction(
    label: String,
    icon: ImageVector,
    bg: Color,
    tint: Color,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(dims.radius.r12))
                .background(bg)
                .debouncedClickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(dims.sizing.iconLg)
            )
        }
        PMText(text = label, style = PMTextStyle.Note, color = colors.textLabel)
    }
}
