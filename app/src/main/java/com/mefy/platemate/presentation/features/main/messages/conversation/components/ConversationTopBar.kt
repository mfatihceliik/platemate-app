package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ConversationTopBar(
    participantName: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface)
                .padding(
                    start = dims.spacing.s4,
                    end = dims.spacing.s8,
                    top = dims.spacing.s4,
                    bottom = dims.spacing.s12
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMIconButton(onClick = onBackClick) {
                PMIcon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.common_back),
                    tint = colors.textPrimary
                )
            }

            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarIconInner)
                    .clip(CircleShape)
                    .background(avatarBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = initials,
                    style = PMTextStyle.Caption,
                    fontWeight = FontWeight.Bold,
                    color = avatarFg
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = participantName,
                    style = PMTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }

            PMIconButton(onClick = onInfoClick) {
                PMIcon(
                    imageVector = Icons.Outlined.Info,
                    tint = colors.textLabel
                )
            }
        }
        HorizontalDivider(color = colors.outlineVariant)
    }
}

@Preview(name = "ConversationTopBar Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ConversationTopBarLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ConversationTopBar(
            participantName = "Ahmet Yılmaz",
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
            onBackClick = {},
            onInfoClick = {}
        )
    }
}

@Preview(name = "ConversationTopBar Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ConversationTopBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ConversationTopBar(
            participantName = "Ahmet Yılmaz",
            initials = "AY",
            avatarBg = Color(0xFF164E63),
            avatarFg = Color(0xFF67E8F9),
            onBackClick = {},
            onInfoClick = {}
        )
    }
}
