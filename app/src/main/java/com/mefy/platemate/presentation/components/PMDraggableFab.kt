package com.mefy.platemate.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import kotlin.math.roundToInt

@Composable
fun PMDraggableFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    containerColor: Color = MaterialTheme.pmColors.primary,
    contentColor: Color = MaterialTheme.pmColors.onPrimary,
    shape: Shape = CircleShape,
    size: Dp = 56.dp,
    elevation: Dp = 6.dp
) {
    var offsetX by rememberSaveable { mutableFloatStateOf(0f) }
    var offsetY by rememberSaveable { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "fab_scale_animation"
    )
    
    val currentElevation by animateFloatAsState(
        targetValue = if (isDragging) (elevation.value * 2) else elevation.value,
        animationSpec = spring(),
        label = "fab_elevation_animation"
    )

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .scale(scale)
            .shadow(elevation = currentElevation.dp, shape = shape)
            .clip(shape)
            .background(containerColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { _ -> isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
            .debouncedClickable(onClick = onClick)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        PMIcon(
            imageVector = icon,
            tint = contentColor,
            modifier = Modifier.size(size * 0.45f)
        )
    }
}

@Preview(name = "Draggable FAB Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMDraggableFabPreview() {
    com.mefy.platemate.presentation.theme.PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            PMDraggableFab(onClick = {})
        }
    }
}

@Preview(name = "Draggable FAB Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMDraggableFabDarkPreview() {
    com.mefy.platemate.presentation.theme.PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            PMDraggableFab(onClick = {})
        }
    }
}
