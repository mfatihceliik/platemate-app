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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ChatDetailUserCard(
    participantName: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBlockClick: () -> Unit,
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
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(dims.radius.r12))
                .debouncedClickable(onClick = onProfileClick)
                .padding(dims.spacing.s4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
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

            PMText(
                text = participantName,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            QuickAction(
                label = stringResource(R.string.chatdetail_action_message),
                icon = Icons.Outlined.ChatBubbleOutline,
                bg = colors.primaryContainer,
                tint = colors.primary,
                onClick = onMessageClick
            )
            QuickAction(
                label = stringResource(R.string.chatdetail_action_profile),
                icon = Icons.Outlined.Person,
                bg = colors.categoryGreenBg,
                tint = colors.categoryGreenFg,
                onClick = onProfileClick
            )
            QuickAction(
                label = stringResource(R.string.chatdetail_action_block),
                icon = Icons.Outlined.Block,
                bg = colors.errorContainer,
                tint = colors.error,
                onClick = onBlockClick
            )
        }
    }
}

@Preview(name = "ChatDetailUserCard Light", showBackground = true, backgroundColor = 0xFFF1F5F9)
@Composable
private fun ChatDetailUserCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ChatDetailUserCard(
            participantName = "Ahmet Yılmaz",
            initials = "AY",
            avatarBg = Color(0xFFEEF2FF),
            avatarFg = Color(0xFF4F46E5),
            onMessageClick = {},
            onProfileClick = {},
            onBlockClick = {}
        )
    }
}

@Preview(name = "ChatDetailUserCard Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ChatDetailUserCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ChatDetailUserCard(
            participantName = "Zeynep Demir",
            initials = "ZD",
            avatarBg = Color(0xFF312E81),
            avatarFg = Color(0xFFC7D2FE),
            onMessageClick = {},
            onProfileClick = {},
            onBlockClick = {}
        )
    }
}
