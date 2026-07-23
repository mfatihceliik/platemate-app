package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIconContainer
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ConversationEmptyState(
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(sizing.plateBadgeMd)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            PMIconContainer(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                iconSize = sizing.iconXl
            )
        }

        PMText(
            text = stringResource(R.string.conversation_empty_title),
            fontSize = fontSize.md,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = spacing.s12)
        )
        PMText(
            text = stringResource(R.string.conversation_empty_subtitle),
            fontSize = fontSize.md,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = spacing.s4)
        )
    }
}

@Preview(name = "Conversation Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ConversationEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ConversationEmptyState()
    }
}
