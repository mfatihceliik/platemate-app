package com.mefy.platemate.presentation.features.main.messages.chatdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ChatDetailUserCard(
    modifier: Modifier = Modifier,
    participantName: String,
    onMessageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBlockClick: () -> Unit
) {
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shape = PMTheme.shapes

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = stroke.st2, shape = shape.medium)
            .background(colors.surface, shape = shape.medium)
            .border(stroke.st1, colors.outlineVariant, shape = shape.medium)
            .padding(spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        Column(
            modifier = Modifier
                .clip(shape.medium)
                .debouncedClickable(onClick = onProfileClick)
                .padding(spacing.s4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {

            PMAvatar(
                displayName = participantName,
                size = sizing.avatarLg
            )

            PMText(
                text = participantName,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.s16)
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
            onMessageClick = {},
            onProfileClick = {},
            onBlockClick = {}
        )
    }
}
