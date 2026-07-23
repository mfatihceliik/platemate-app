package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun StatBox(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    color: Color
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes.medium

    Column(
        modifier = modifier
            .background(colors.surface, shape)
            .padding(vertical = spacing.s12, horizontal = spacing.s8)
            .shadow(spacing.s8, spotColor = colors.cardShadow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        PMText(text = value, style = PMTextStyle.Display, color = color)
        PMText(text = label, style = PMTextStyle.Caption, color = colors.textTertiary, textAlign = TextAlign.Center)
    }
}
