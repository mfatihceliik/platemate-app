package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import com.mefy.platemate.domain.model.chat.MessageStatus
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMQuotedMessageCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.messages.conversation.ReplyPreviewUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

private val ReceivedShape = RoundedCornerShape(
    topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 4.dp
)
private val SentShape = RoundedCornerShape(
    topStart = 8.dp, topEnd = 8.dp, bottomEnd = 4.dp, bottomStart = 8.dp
)

@Composable
internal fun ReceivedBubble(
    modifier: Modifier = Modifier,
    senderName: String,
    content: String,
    time: String,
    quotedPreview: ReplyPreviewUiModel? = null
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
        verticalAlignment = Alignment.Bottom
    ) {

        PMAvatar(
            displayName = senderName,
            size = sizing.avatarSm
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s4),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(colors.surfaceVariant, ReceivedShape)
                    .padding(horizontal = spacing.s12, vertical = spacing.s10)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    if (quotedPreview != null) {
                        PMQuotedMessageCard(
                            senderLabel = quotedPreview.senderLabel.resolve(),
                            contentPreview = quotedPreview.contentPreview,
                            accentColor = colors.primary,
                            backgroundColor = colors.primary.copy(alpha = 0.1f),
                            textColor = colors.textPrimary
                        )
                    }
                    PMText(
                        text = content,
                        style = PMTextStyle.Body,
                        color = colors.textPrimary
                    )
                }
            }
            PMText(
                text = time,
                style = PMTextStyle.Note,
                color = colors.textSecondary,
                modifier = Modifier.padding(start = spacing.s4)
            )
        }
    }
}

@Composable
internal fun SentBubble(
    content: String,
    time: String,
    status: MessageStatus,
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit = {},
    quotedPreview: ReplyPreviewUiModel? = null
) {
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val shape = PMTheme.shapes

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s4),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .background(colors.primary, SentShape)
                    .padding(horizontal = spacing.s12, vertical = spacing.s10)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    if (quotedPreview != null) {
                        PMQuotedMessageCard(
                            senderLabel = quotedPreview.senderLabel.resolve(),
                            contentPreview = quotedPreview.contentPreview,
                            accentColor = colors.onPrimary,
                            backgroundColor = colors.onPrimary.copy(alpha = 0.15f),
                            textColor = colors.onPrimary
                        )
                    }
                    PMText(
                        text = content,
                        style = PMTextStyle.Body,
                        color = colors.onPrimary
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = time,
                    style = PMTextStyle.Note,
                    color = colors.textSecondary
                )
                // Clock = pending (not yet acked), single check = sent, double check =
                // delivered (grey) / read (primary), red exclamation = failed (tap to retry).
                when (status) {
                    MessageStatus.PENDING -> PMIcon(
                        imageVector = Icons.Default.Schedule,
                        tint = colors.textLabel,
                        size = sizing.iconSm,
                    )
                    MessageStatus.SENT -> PMIcon(
                        imageVector = Icons.Default.Check,
                        tint = colors.iconDefault,
                    )
                    MessageStatus.DELIVERED -> PMIcon(
                        imageVector = Icons.Default.DoneAll,
                        tint = colors.iconDefault,
                    )
                    MessageStatus.READ -> PMIcon(
                        imageVector = Icons.Default.DoneAll,
                        tint = colors.primary,
                    )
                    MessageStatus.FAILED -> PMIcon(
                        imageVector = Icons.Default.ErrorOutline,
                        tint = colors.iconDanger,
                        modifier = Modifier.clickable(onClick = onRetryClicked),
                    )
                    MessageStatus.DELETED -> Unit
                }
            }
        }
    }
}

@Composable
internal fun DeletedBubble(
    isMine: Boolean,
    time: String,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = if (isMine) SentShape else ReceivedShape

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s4),
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(colors.surfaceVariant, shape)
                    .padding(horizontal = spacing.s12, vertical = spacing.s10)
            ) {
                PMText(
                    text = stringResource(R.string.conversation_message_deleted),
                    style = PMTextStyle.Body,
                    color = colors.textTertiary
                )
            }
            PMText(
                text = time,
                style = PMTextStyle.Note,
                color = colors.textSecondary,
                modifier = Modifier.padding(horizontal = spacing.s4)
            )
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
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        ReceivedBubble(
            senderName = "Ahmet Yılmaz",
            content = "Merhaba, plakamı gördünüz mü?",
            time = "10:42"
        )
        SentBubble(
            content = "Evet, 34 EK 0682 değil mi?",
            time = "10:43",
            status = MessageStatus.READ
        )
        ReceivedBubble(
            senderName = "Ahmet Yılmaz",
            content = "Evet aynen o! Teşekkürler.",
            time = "10:44"
        )
        SentBubble(
            content = "Rica ederim, iyi günler!",
            time = "10:45",
            status = MessageStatus.SENT
        )
        ReceivedBubble(
            senderName = "Ahmet Yılmaz",
            content = "Tamamdır, hafta sonu da müsaitim.",
            time = "10:46",
            quotedPreview = ReplyPreviewUiModel(
                messageId = 1L,
                senderLabel = UiText.Dynamic("Sen"),
                contentPreview = "Rica ederim, iyi günler!"
            )
        )
        SentBubble(
            content = "Süper, o zaman cumartesi konuşuruz.",
            time = "10:47",
            status = MessageStatus.READ,
            quotedPreview = ReplyPreviewUiModel(
                messageId = 2L,
                senderLabel = UiText.Dynamic("Ahmet Yılmaz"),
                contentPreview = "Tamamdır, hafta sonu da müsaitim."
            )
        )
    }
}
