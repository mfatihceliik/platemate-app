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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

private val ReceivedShape = RoundedCornerShape(
    topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 4.dp
)
private val SentShape = RoundedCornerShape(
    topStart = 8.dp, topEnd = 8.dp, bottomEnd = 4.dp, bottomStart = 8.dp
)

@Composable
internal fun ReceivedBubble(
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    content: String,
    time: String,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(avatarBg),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = initials,
                style = PMTextStyle.Note,
                fontWeight = FontWeight.Bold,
                color = avatarFg
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(colors.surfaceVariant, ReceivedShape)
                    .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s10)
            ) {
                PMText(
                    text = content,
                    style = PMTextStyle.Body,
                    color = colors.textPrimary
                )
            }
            PMText(
                text = time,
                style = PMTextStyle.Note,
                color = colors.textLabel,
                modifier = Modifier.padding(start = dims.spacing.s4)
            )
        }
    }
}

@Composable
internal fun SentBubble(
    content: String,
    time: String,
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary
    val onPrimary = colors.onPrimary

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .background(primary, SentShape)
                    .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s10)
            ) {
                PMText(
                    text = content,
                    style = PMTextStyle.Body,
                    color = onPrimary
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(text = time, style = PMTextStyle.Note, color = colors.textLabel)
                if (isRead) {
                    PMIcon(
                        imageVector = Icons.Default.Check,
                        tint = primary,
                    )
                }
            }
        }
    }
}

@Preview(name = "ChatBubble Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ChatBubbleLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ChatBubblePreviewContent()
    }
}

@Preview(name = "ChatBubble Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ChatBubbleDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ChatBubblePreviewContent()
    }
}

@Composable
private fun ChatBubblePreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.pmColors.background)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        ReceivedBubble(
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
            content = "Merhaba, plakamı gördünüz mü?",
            time = "10:42"
        )
        SentBubble(
            content = "Evet, 34 EK 0682 değil mi?",
            time = "10:43",
            isRead = true
        )
        ReceivedBubble(
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
            content = "Evet aynen o! Teşekkürler.",
            time = "10:44"
        )
        SentBubble(
            content = "Rica ederim, iyi günler!",
            time = "10:45",
            isRead = false
        )
    }
}
