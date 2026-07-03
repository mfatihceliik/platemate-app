package com.mefy.platemate.presentation.common.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle

@Composable
internal fun LabelOverlay(
    modifier: Modifier = Modifier,
    visible: Boolean,
    label: String,
    color: Color,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(tween(320)) + expandVertically(tween(320, easing = FastOutSlowInEasing)),
        exit = fadeOut(tween(240)) + shrinkVertically(tween(240, easing = FastOutSlowInEasing))
    ) {
        PMText(
            text = label,
            style = PMTextStyle.Note,
            color = color
        )
    }
}