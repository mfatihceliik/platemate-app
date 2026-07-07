package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Circular "jump to newest message" button overlaid on the conversation list. Fades/scales
 * in when [visible]; the caller positions it via [modifier] (e.g. `align(BottomEnd)`).
 */
@Composable
internal fun ScrollToBottomButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.scrollToBottomButton)
                .shadow(dims.spacing.s8, CircleShape)
                .clip(CircleShape)
                .background(colors.surface)
                .debouncedClickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Default.KeyboardArrowDown,
                tint = colors.textLabel,
                size = dims.sizing.iconLg
            )
        }
    }
}

@Preview(name = "ScrollToBottomButton Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ScrollToBottomButtonLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ScrollToBottomButton(visible = true, onClick = {})
    }
}

@Preview(name = "ScrollToBottomButton Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ScrollToBottomButtonDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ScrollToBottomButton(visible = true, onClick = {})
    }
}
