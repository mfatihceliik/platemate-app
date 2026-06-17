package com.mefy.platemate.presentation.components.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

/**
 * Wraps an onClick lambda with a debounce mechanism to prevent rapid double clicks.
 * State is remembered across recompositions safely.
 */
@Composable
fun debouncedClick(
    debounceMillis: Long = 600L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    return {
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= debounceMillis) {
            lastClickTime = now
            onClick()
        }
    }
}

/**
 * A modifier that applies debounced clickable behavior using Compose best practices.
 */
fun Modifier.debouncedClickable(
    enabled: Boolean = true,
    debounceMillis: Long = 600L,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "debouncedClickable"
        properties["enabled"] = enabled
        properties["debounceMillis"] = debounceMillis
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    Modifier.clickable(
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

/**
 * Overloaded debouncedClickable that accepts custom interaction sources and indications.
 */
fun Modifier.debouncedClickable(
    interactionSource: MutableInteractionSource,
    indication: androidx.compose.foundation.Indication?,
    enabled: Boolean = true,
    debounceMillis: Long = 600L,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "debouncedClickable"
        properties["interactionSource"] = interactionSource
        properties["indication"] = indication
        properties["enabled"] = enabled
        properties["debounceMillis"] = debounceMillis
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = indication,
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
