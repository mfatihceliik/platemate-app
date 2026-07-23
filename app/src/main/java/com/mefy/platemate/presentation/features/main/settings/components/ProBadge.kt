package com.mefy.platemate.presentation.features.main.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun ProBadge() {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shapes = PMTheme.shapes
    Box(
        modifier = Modifier
            .clip(shapes.medium)
            .background(colors.warning)
            .padding(horizontal = spacing.s8, vertical = spacing.s4)
    ) {
        PMText(
            text = "PRO",
            style = PMTextStyle.Note,
            fontWeight = FontWeight.Bold,
            color = colors.onPrimary
        )
    }
}