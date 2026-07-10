package com.mefy.platemate.presentation.components

import androidx.compose.animation.core.animate
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
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.launch


private const val REVEAL_FRACTION = 0.25f
private const val FULL_SWIPE_FRACTION = 0.5f
private const val VELOCITY_PROJECTION = 0.05f
@Immutable
data class PMSwipeAction(
    val icon: ImageVector,
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
)

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

        var offsetX by remember { mutableFloatStateOf(0f) }
        var rawDrag by remember { mutableFloatStateOf(0f) }
        val scope = rememberCoroutineScope()

        val anchors = remember(minOffset, maxOffset) {
            buildList {
                if (minOffset < 0f) add(minOffset)
                add(0f)
                if (maxOffset > 0f) add(maxOffset)
            }
        }

        val dragState = rememberDraggableState { delta ->
            rawDrag += delta
            offsetX = (offsetX + delta).coerceIn(minOffset, maxOffset)
        }

        if (offsetX < 0f && endToStartAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterEnd) {
                SwipeActionButton(action = endToStartAction, width = revealDp) {
                    onEndToStart?.invoke()
                    scope.launch { animate(offsetX, 0f) { value, _ -> offsetX = value } }
                }
            }
        }
        if (offsetX > 0f && startToEndAction != null) {
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.CenterStart) {
                SwipeActionButton(action = startToEndAction, width = revealDp) {
                    onStartToEnd?.invoke()
                    scope.launch { animate(offsetX, 0f) { value, _ -> offsetX = value } }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .background(backgroundColor)
                .draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    enabled = minOffset != 0f || maxOffset != 0f,
                    onDragStarted = { rawDrag = offsetX },
                    onDragStopped = { velocity ->
                        val target = when {
                            startToEndAction != null && rawDrag >= triggerPx -> {
                                onStartToEnd?.invoke(); 0f
                            }

                            endToStartAction != null && rawDrag <= -triggerPx -> {
                                onEndToStart?.invoke(); 0f
                            }

                            else -> {
                                val projected = offsetX + velocity * VELOCITY_PROJECTION
                                anchors.minByOrNull { abs(it - projected) } ?: 0f
                            }
                        }
                        animate(offsetX, target, initialVelocity = velocity) { value, _ ->
                            offsetX = value
                        }
                    }
                )
        ) {
            content()
            if (offsetX != 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { scope.launch { animate(offsetX, 0f) { value, _ -> offsetX = value } } }
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
