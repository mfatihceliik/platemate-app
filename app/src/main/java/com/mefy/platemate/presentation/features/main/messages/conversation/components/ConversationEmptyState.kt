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
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconContainer
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ConversationEmptyState(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dims.spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.plateBadgeMd)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            PMIconContainer(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                iconSize = dims.sizing.iconXl
            )
        }

        PMText(
            text = stringResource(R.string.conversation_empty_title),
            fontSize = dims.fontSize.md,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = dims.spacing.s12)
        )
        PMText(
            text = stringResource(R.string.conversation_empty_subtitle),
            fontSize = dims.fontSize.md,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = dims.spacing.s4)
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
