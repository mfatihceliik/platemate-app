package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlin.math.roundToInt

private enum class ReplyAnchor { Closed, Triggered }

private const val TRIGGER_POSITIONAL_FRACTION = 0.9f


@Composable
internal fun SwipeToReplyBox(
    modifier: Modifier = Modifier,
    onReply: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current

    val maxDragPx = with(density) { spacing.s32.toPx() }

    val state = remember { AnchoredDraggableState(initialValue = ReplyAnchor.Closed) }
    val anchors = remember(maxDragPx) {
        DraggableAnchors {
            ReplyAnchor.Closed at 0f
            ReplyAnchor.Triggered at maxDragPx
        }
    }
    SideEffect { state.updateAnchors(anchors) }

    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { totalDistance -> totalDistance * TRIGGER_POSITIONAL_FRACTION }
    )

    LaunchedEffect(state, onReply) {
        snapshotFlow { state.settledValue }.collect { settled ->
            if (settled == ReplyAnchor.Triggered) {
                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                onReply()
                state.animateTo(ReplyAnchor.Closed)
            }
        }
    }

    val offsetPx = if (state.offset.isNaN()) 0f else state.offset
    val progress = (offsetPx / maxDragPx).coerceIn(0f, 1f)

    Box(modifier = modifier) {
        PMIcon(
            imageVector = Icons.AutoMirrored.Filled.Reply,
            tint = colors.primary,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .graphicsLayer {
                    alpha = progress
                    scaleX = 0.6f + 0.4f * progress
                    scaleY = 0.6f + 0.4f * progress
                }
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetPx.roundToInt(), 0) }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                )
        ) {
            content()
        }
    }
}

@Preview(name = "SwipeToReplyBox Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SwipeToReplyBoxLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SwipeToReplyBoxPreviewContent()
    }
}

@Preview(name = "SwipeToReplyBox Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SwipeToReplyBoxDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SwipeToReplyBoxPreviewContent()
    }
}

@Composable
private fun SwipeToReplyBoxPreviewContent() {
    SwipeToReplyBox(onReply = {}) {
        SentBubble(
            content = "Merhaba, nasılsın?",
            time = "10:42",
            status = MessageStatus.READ
        )
    }
}
