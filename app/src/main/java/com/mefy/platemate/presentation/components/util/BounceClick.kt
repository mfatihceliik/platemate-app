package com.mefy.platemate.presentation.components.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.focus.focusProperties

/**
 * A modifier that applies a bounce (scale down) animation on press,
 * completely replacing the default ripple effect.
 * It also includes the debounce logic to prevent rapid double clicks.
 */
fun Modifier.bounceClick(
    enabled: Boolean = true,
    debounceMillis: Long = 600L,
    scaleDown: Float = 0.95f,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    androidx.compose.runtime.LaunchedEffect(isPressed) {
        if (isPressed && enabled) {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) scaleDown else 1f,
        label = "bounceClickScale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .focusProperties { canFocus = false }
        .clickable(
            interactionSource = interactionSource,
            indication = null, // Standart gri dalgalanmayı kapatır
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            onClick = {
                val now = System.currentTimeMillis()
                if (now - lastClickTime >= debounceMillis) {
                    lastClickTime = now
                    onClick()
                }
            }
        )
}
