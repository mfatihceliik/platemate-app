package com.mefy.platemate.presentation.features.main.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.messages.MessagesScreen
import com.mefy.platemate.presentation.features.main.messages.MessagesUiState
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun MessagesEmptyState(
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    Column(
        modifier = modifier.padding(spacing.s32),
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
            PMIcon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = colors.textLabel,
                size = sizing.iconXl,
            )
        }

        PMText(
            text = stringResource(R.string.messages_empty_title),
            fontSize = fontSize.md,
            color = colors.textPrimary,
            modifier = Modifier.padding(top = spacing.s12)
        )
        PMText(
            text = stringResource(R.string.messages_empty_subtitle),
            fontSize = fontSize.md,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = spacing.s4)
        )
    }
}

@Preview(name = "Messages Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessagesEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessagesScreen(
            state = MessagesUiState(isLoading = false),
            onAction = {},
            innerPadding = PaddingValues()
        )
    }
}