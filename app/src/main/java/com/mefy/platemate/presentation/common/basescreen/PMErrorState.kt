package com.mefy.platemate.presentation.common.basescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun PMErrorState(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: UiText = UiText.Resource(R.string.common_error_state_title)
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize

    Column(
        modifier = modifier
            .background(colors.background)
            .padding(spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s12, Alignment.CenterVertically)
    ) {
        PMIcon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = colors.textTertiary,
            size = sizing.iconXl
        )
        PMText(
            text = title.resolve(),
            fontSize = fontSize.lg,
            color = colors.onSurface,
            textAlign = TextAlign.Center
        )
        PMText(
            text = message.resolve(),
            fontSize = fontSize.md,
            color = colors.textTertiary,
            textAlign = TextAlign.Center
        )
        PMButton(
            text = UiText.Resource(R.string.common_retry).resolve(),
            onClick = onRetry
        )
    }
}
