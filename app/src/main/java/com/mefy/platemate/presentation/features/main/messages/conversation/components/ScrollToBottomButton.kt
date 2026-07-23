package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ScrollToBottomButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onClick: () -> Unit
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {

        PMIconButton(
            imageVector = Icons.Default.KeyboardArrowDown,
            onClick = onClick,
            variant = PMIconButtonVariant.Outlined,
            size = sizing.iconLg,
            containerColor = colors.surface
        )
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
