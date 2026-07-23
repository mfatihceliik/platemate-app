package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.messages.conversation.ChatMessageUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme


@Composable
internal fun MessageActionMenu(
    modifier: Modifier = Modifier,
    message: ChatMessageUiModel,
    participantName: String,
    anchorBounds: Rect,
    onDismiss: () -> Unit,
    onQuoteClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes
    val density = LocalDensity.current

    BackHandler(onBack = onDismiss)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.scrim.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidthPx = with(density) { maxWidth.toPx() }
            val screenHeightPx = with(density) { maxHeight.toPx() }
            val marginPx = with(density) { spacing.s16.toPx() }
            val gapPx = with(density) { spacing.s8.toPx() }

            var cardHeightPx by remember { mutableIntStateOf(with(density) { 96.dp.roundToPx() }) }
            val cardWidthPx = with(density) { 220.dp.toPx() }.coerceAtMost(screenWidthPx - marginPx * 2)
            val cardWidthDp = with(density) { cardWidthPx.toDp() }

            val spaceBelow = screenHeightPx - anchorBounds.bottom
            val showBelow = spaceBelow >= cardHeightPx + marginPx

            val cardTopPx = if (showBelow) {
                anchorBounds.bottom + gapPx
            } else {
                anchorBounds.top - gapPx - cardHeightPx
            }.coerceIn(marginPx, (screenHeightPx - cardHeightPx - marginPx).coerceAtLeast(marginPx))

            val cardLeftPx = if (message.isMine) {
                anchorBounds.right - cardWidthPx - gapPx
            } else {
                anchorBounds.left + gapPx
            }.coerceIn(marginPx, (screenWidthPx - cardWidthPx - marginPx).coerceAtLeast(marginPx))

            var scaleTarget by remember { mutableStateOf(1f) }
            LaunchedEffect(Unit) { scaleTarget = 1.05f }
            val bubbleScale by animateFloatAsState(
                targetValue = scaleTarget,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "messageActionMenuBubbleScale"
            )

            Box(
                modifier = Modifier
                    .offset { IntOffset(anchorBounds.left.toInt(), anchorBounds.top.toInt()) }
                    .width(with(density) { anchorBounds.width.toDp() })
                    .graphicsLayer {
                        scaleX = bubbleScale
                        scaleY = bubbleScale
                    }
            ) {
                if (message.isMine) {
                    SentBubble(
                        content = message.content,
                        time = message.time,
                        status = message.status,
                        modifier = Modifier.padding(horizontal = spacing.s4),
                        quotedPreview = message.replyPreview
                    )
                } else {
                    ReceivedBubble(
                        senderName = participantName,
                        content = message.content,
                        time = message.time,
                        modifier = Modifier.padding(horizontal = spacing.s4),
                        quotedPreview = message.replyPreview
                    )
                }
            }

            Column(
                modifier = Modifier
                    .offset { IntOffset(cardLeftPx.toInt(), cardTopPx.toInt()) }
                    .width(cardWidthDp)
                    .onSizeChanged { cardHeightPx = it.height }
                    .clip(shape.medium)
                    .background(colors.surface)
            ) {
                MessageActionMenuRow(
                    icon = Icons.AutoMirrored.Filled.Reply,
                    label = stringResource(R.string.conversation_action_quote),
                    onClick = onQuoteClick
                )
                if (!message.isMine) {
                    MessageActionMenuRow(
                        icon = Icons.Filled.Flag,
                        label = stringResource(R.string.conversation_action_report),
                        onClick = onReportClick
                    )
                }
                if (message.isMine) {
                    MessageActionMenuRow(
                        icon = Icons.Filled.Delete,
                        label = stringResource(R.string.common_delete),
                        onClick = onDeleteClick,
                        tint = colors.error
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageActionMenuRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color? = null
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val contentColor = tint ?: colors.textPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = spacing.s16, vertical = spacing.s12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        PMIcon(
            imageVector = icon,
            tint = contentColor
        )
        PMText(
            text = label,
            style = PMTextStyle.Body,
            color = contentColor
        )
    }
}

private val messageActionMenuPreviewMineMessage = ChatMessageUiModel(
    id = 1L,
    content = "Yarın saat 5'te müsait misin?",
    time = "14:32",
    isMine = true,
    status = MessageStatus.DELIVERED
)

private val messageActionMenuPreviewOtherMessage = ChatMessageUiModel(
    id = 2L,
    content = "Evet, müsaitim. Nerede buluşalım?",
    time = "14:33",
    isMine = false,
    status = MessageStatus.DELIVERED
)

@Preview(name = "MessageActionMenu Mine", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessageActionMenuMinePreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Box(modifier = Modifier.size(360.dp, 640.dp)) {
            MessageActionMenu(
                message = messageActionMenuPreviewMineMessage,
                participantName = "Ayşe Yılmaz",
                anchorBounds = Rect(left = 120f, top = 260f, right = 320f, bottom = 320f),
                onDismiss = {},
                onQuoteClick = {},
                onReportClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(name = "MessageActionMenu Other", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun MessageActionMenuOtherPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.size(360.dp, 640.dp)) {
            MessageActionMenu(
                message = messageActionMenuPreviewOtherMessage,
                participantName = "Ayşe Yılmaz",
                anchorBounds = Rect(left = 40f, top = 260f, right = 260f, bottom = 320f),
                onDismiss = {},
                onQuoteClick = {},
                onReportClick = {},
                onDeleteClick = {}
            )
        }
    }
}
