package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

private const val REVEAL_FRACTION = 0.25f
private const val FULL_SWIPE_FRACTION = 0.5f

@Immutable
data class PMSwipeAction(
    val icon: ImageVector,
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
)

private enum class SwipeAnchor { Closed, RevealedStart, RevealedEnd, TriggerStart, TriggerEnd }

@Composable
fun PMSwipeActionRow(
    modifier: Modifier = Modifier,
    startToEndAction: PMSwipeAction? = null,
    endToStartAction: PMSwipeAction? = null,
    onStartToEnd: (() -> Unit)? = null,
    onEndToStart: (() -> Unit)? = null,
    backgroundColor: Color = PMTheme.colors.background,
    content: @Composable () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val revealDp: Dp = maxWidth * REVEAL_FRACTION
        val revealPx = with(density) { revealDp.toPx() }
        val triggerPx = with(density) { maxWidth.toPx() } * FULL_SWIPE_FRACTION

        val state = remember { AnchoredDraggableState(initialValue = SwipeAnchor.Closed) }

        val anchors = remember(revealPx, triggerPx, startToEndAction, endToStartAction) {
            DraggableAnchors {
                SwipeAnchor.Closed at 0f
                if (startToEndAction != null) {
                    SwipeAnchor.RevealedStart at revealPx
                    SwipeAnchor.TriggerStart at triggerPx
                }
                if (endToStartAction != null) {
                    SwipeAnchor.RevealedEnd at -revealPx
                    SwipeAnchor.TriggerEnd at -triggerPx
                }
            }
        }
        SideEffect { state.updateAnchors(anchors) }

        // Bir tetik-anchor'da (tam çekme) kararlı hale gelince: haptic + aksiyon + kapan.
        LaunchedEffect(state, onStartToEnd, onEndToStart) {
            snapshotFlow { state.settledValue }.collect { settled ->
                when (settled) {
                    SwipeAnchor.TriggerStart -> {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onStartToEnd?.invoke()
                        state.animateTo(SwipeAnchor.Closed)
                    }

                    SwipeAnchor.TriggerEnd -> {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onEndToStart?.invoke()
                        state.animateTo(SwipeAnchor.Closed)
                    }

                    else -> Unit
                }
            }
        }

        val offsetPx = if (state.offset.isNaN()) 0f else state.offset

        if (offsetPx < 0f && endToStartAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterEnd) {
                SwipeActionButton(action = endToStartAction, width = revealDp) {
                    onEndToStart?.invoke()
                    scope.launch { state.animateTo(SwipeAnchor.Closed) }
                }
            }
        }
        if (offsetPx > 0f && startToEndAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterStart) {
                SwipeActionButton(action = startToEndAction, width = revealDp) {
                    onStartToEnd?.invoke()
                    scope.launch { state.animateTo(SwipeAnchor.Closed) }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetPx.roundToInt(), 0) }
                .background(backgroundColor)
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    enabled = startToEndAction != null || endToStartAction != null
                )
        ) {
            content()
            if (offsetPx != 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { scope.launch { state.animateTo(SwipeAnchor.Closed) } }
                )
            }
        }
    }
}

@Composable
private fun SwipeActionButton(action: PMSwipeAction, width: Dp, onClick: () -> Unit) {
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .background(action.containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMIcon(imageVector = action.icon, contentDescription = null, tint = action.contentColor)
            PMText(
                text = action.label,
                color = action.contentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize.sm
            )
        }
    }
}



@Preview(name = "PMSwipeActionRow", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMSwipeActionRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = PMTheme.colors
        Column(modifier = Modifier.fillMaxWidth()) {
            PMSwipeActionRow(
                startToEndAction = PMSwipeAction(
                    icon = Icons.Filled.DoneAll,
                    label = "Okundu",
                    containerColor = colors.success,
                    contentColor = Color.White
                ),
                endToStartAction = PMSwipeAction(
                    icon = Icons.Filled.Delete,
                    label = "Sil",
                    containerColor = colors.error,
                    contentColor = colors.onError
                ),
                onStartToEnd = {},
                onEndToStart = {}
            ) {
                PMMessageItem(
                    name = "Ahmet Y.", preview = "Tesekkurler!",
                    time = "09:24", unreadCount = 3,
                    onClick = {}
                )
            }
            // Read row — delete only.
            PMSwipeActionRow(
                endToStartAction = PMSwipeAction(
                    icon = Icons.Filled.Delete,
                    label = "Sil",
                    containerColor = colors.error,
                    contentColor = colors.onError
                ),
                onEndToStart = {}
            ) {
                PMMessageItem(
                    name = "Zeynep K.", preview = "Gorusuruz",
                    time = "Dun", unreadCount = 0,
                    onClick = {}
                )
            }
        }
    }
}
