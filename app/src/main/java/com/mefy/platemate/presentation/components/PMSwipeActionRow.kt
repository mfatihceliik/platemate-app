package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

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
 * A reusable row that reveals a colored action while the user swipes, fires it once the swipe
 * passes the threshold, then snaps back — the row itself never dismisses. Any list feeds it two
 * optional actions and their callbacks; the row owns only the gesture and its visual reveal.
 *
 * - [startToEndAction] is revealed on the leading edge when swiping right (start → end).
 * - [endToStartAction] is revealed on the trailing edge when swiping left (end → start).
 *
 * A direction is only swipeable when its action is non-null, so a row can offer one side, both, or
 * neither.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMSwipeActionRow(
    modifier: Modifier = Modifier,
    startToEndAction: PMSwipeAction? = null,
    endToStartAction: PMSwipeAction? = null,
    onStartToEnd: (() -> Unit)? = null,
    onEndToStart: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val state = rememberSwipeToDismissBoxState(
        // ~35% of the width triggers the action — lower than the 50% default so it feels
        // responsive, but high enough not to fire on an accidental brush.
        positionalThreshold = { totalDistance -> totalDistance * SWIPE_ACTION_THRESHOLD },
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> onStartToEnd?.invoke()
                SwipeToDismissBoxValue.EndToStart -> onEndToStart?.invoke()
                SwipeToDismissBoxValue.Settled -> Unit
            }
            // Never let the box actually remove the row: actions only trigger. The row snaps back
            // and the list updates reactively once the underlying data changes.
            false
        }
    )

    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        enableDismissFromStartToEnd = startToEndAction != null,
        enableDismissFromEndToStart = endToStartAction != null,
        backgroundContent = {
            val isStartToEnd = state.dismissDirection == SwipeToDismissBoxValue.StartToEnd
            val action = if (isStartToEnd) startToEndAction else endToStartAction
            SwipeActionBackground(action = action, isStartToEnd = isStartToEnd)
        },
        content = { content() }
    )
}

@Composable
private fun SwipeActionBackground(action: PMSwipeAction?, isStartToEnd: Boolean) {
    val dims = MaterialTheme.pmDimensions

    // While settled there is no active direction; render an empty backdrop so nothing flashes.
    if (action == null) {
        Box(Modifier.fillMaxSize())
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(action.containerColor)
            .padding(horizontal = dims.spacing.s24),
        contentAlignment = if (isStartToEnd) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        val icon = @Composable {
            PMIcon(imageVector = action.icon, contentDescription = null, tint = action.contentColor)
        }
        val label = @Composable {
            PMText(
                text = action.label,
                color = action.contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon leads on the start edge, trails on the end edge, so it always sits toward the
            // screen edge the swipe pulls from.
            if (isStartToEnd) {
                icon()
                label()
            } else {
                label()
                icon()
            }
        }
    }
}

private const val SWIPE_ACTION_THRESHOLD = 0.35f

@Preview(name = "PMSwipeActionRow", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMSwipeActionRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = MaterialTheme.pmColors
        Column(modifier = Modifier.fillMaxWidth()) {
            // Resting row — both actions available; swipe in the interactive preview to reveal them.
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
            // Second row offers only delete (already-read conversation).
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
