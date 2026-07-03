package com.mefy.platemate.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * One swipe-revealed action (icon + label + colors). Purely descriptive; the meaning of the
 * gesture is decided by the caller via [PMSwipeActionRow]'s callbacks.
 */
@Immutable
data class PMSwipeAction(
    val icon: ImageVector,
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * A reveal-style swipe row (iOS-Mail behaviour).
 *
 * - Swiping right reveals [startToEndAction] pinned to the leading edge; swiping left reveals
 *   [endToStartAction] pinned to the trailing edge. The reveal is capped at [REVEAL_FRACTION] of
 *   the row width, so only a small button ever shows.
 * - A gentle swipe **holds the button open**; tapping the button fires its callback, tapping the
 *   row closes it.
 * - A decisive **full swipe** (finger travels past [FULL_SWIPE_FRACTION] of the width) fires the
 *   callback directly.
 *
 * The row never removes itself — callbacks drive the list, which updates reactively. A direction is
 * only swipeable when its action is non-null.
 */
@Composable
fun PMSwipeActionRow(
    modifier: Modifier = Modifier,
    startToEndAction: PMSwipeAction? = null,
    endToStartAction: PMSwipeAction? = null,
    onStartToEnd: (() -> Unit)? = null,
    onEndToStart: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val backgroundColor = MaterialTheme.pmColors.background

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val revealDp: Dp = maxWidth * REVEAL_FRACTION
        val revealPx = with(density) { revealDp.toPx() }
        val triggerPx = with(density) { maxWidth.toPx() } * FULL_SWIPE_FRACTION

        val minOffset = if (endToStartAction != null) -revealPx else 0f
        val maxOffset = if (startToEndAction != null) revealPx else 0f

        val offsetX = remember { Animatable(0f) }
        var rawDrag by remember { mutableFloatStateOf(0f) }
        val scope = rememberCoroutineScope()

        val dragState = rememberDraggableState { delta ->
            rawDrag += delta
            scope.launch { offsetX.snapTo((offsetX.value + delta).coerceIn(minOffset, maxOffset)) }
        }

        // ── Behind: fixed-width action buttons, only the swiped side is drawn ──
        if (offsetX.value < 0f && endToStartAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterEnd) {
                SwipeActionButton(action = endToStartAction, width = revealDp) {
                    onEndToStart?.invoke()
                    scope.launch { offsetX.animateTo(0f) }
                }
            }
        }
        if (offsetX.value > 0f && startToEndAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterStart) {
                SwipeActionButton(action = startToEndAction, width = revealDp) {
                    onStartToEnd?.invoke()
                    scope.launch { offsetX.animateTo(0f) }
                }
            }
        }

        // ── Front: the row itself. Opaque background so the buttons never bleed through at rest. ──
        val isOpen = offsetX.value != 0f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .background(backgroundColor)
                .draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    enabled = minOffset != 0f || maxOffset != 0f,
                    onDragStarted = { rawDrag = offsetX.value },
                    onDragStopped = {
                        when {
                            startToEndAction != null && rawDrag >= triggerPx -> {
                                onStartToEnd?.invoke()
                                offsetX.animateTo(0f)
                            }

                            endToStartAction != null && rawDrag <= -triggerPx -> {
                                onEndToStart?.invoke()
                                offsetX.animateTo(0f)
                            }

                            minOffset < 0f && offsetX.value <= minOffset / 2f ->
                                offsetX.animateTo(minOffset)

                            maxOffset > 0f && offsetX.value >= maxOffset / 2f ->
                                offsetX.animateTo(maxOffset)

                            else -> offsetX.animateTo(0f)
                        }
                    }
                )
        ) {
            content()
            // While open, a tap anywhere on the row closes it instead of activating the content.
            if (isOpen) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { scope.launch { offsetX.animateTo(0f) } }
                )
            }
        }
    }
}

@Composable
private fun SwipeActionButton(action: PMSwipeAction, width: Dp, onClick: () -> Unit) {
    val dims = MaterialTheme.pmDimensions
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
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMIcon(imageVector = action.icon, contentDescription = null, tint = action.contentColor)
            PMText(
                text = action.label,
                color = action.contentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = dims.fontSize.sm
            )
        }
    }
}

private const val REVEAL_FRACTION = 0.25f
private const val FULL_SWIPE_FRACTION = 0.5f

@Preview(name = "PMSwipeActionRow", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMSwipeActionRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = MaterialTheme.pmColors
        Column(modifier = Modifier.fillMaxWidth()) {
            // Unread row — swipe right (mark read) + swipe left (delete).
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
                    initials = "AY", name = "Ahmet Y.", preview = "Tesekkurler!",
                    time = "09:24", unreadCount = 3,
                    avatarBg = Color(0xFFEEF2FF), avatarFg = Color(0xFF4F46E5),
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
                    initials = "ZK", name = "Zeynep K.", preview = "Gorusuruz",
                    time = "Dun", unreadCount = 0,
                    avatarBg = Color(0xFFECFEFF), avatarFg = Color(0xFF0891B2),
                    onClick = {}
                )
            }
        }
    }
}
