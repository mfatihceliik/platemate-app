package com.mefy.platemate.presentation.features.main.settings.changepassword.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun ValidationRule(
    text: String,
    passed: Boolean
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Box(
            modifier = Modifier
                .size(sizing.iconMd)
                .clip(shapes.medium)
                .background(
                    if (passed) colors.primary
                    else colors.starEmpty
                ),
            contentAlignment = Alignment.Center
        ) {
            if (passed) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        PMText(
            text = text,
            style = PMTextStyle.Caption,
            color = if (passed) colors.textSecondary else colors.textLabel
        )
    }
}
