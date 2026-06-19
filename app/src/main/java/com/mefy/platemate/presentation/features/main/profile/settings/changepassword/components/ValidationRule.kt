package com.mefy.platemate.presentation.features.main.profile.settings.changepassword.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ValidationRule(text: String, passed: Boolean) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.iconMd)
                .clip(RoundedCornerShape(dims.radius.r6))
                .background(
                    if (passed) colors.primary
                    else MaterialTheme.pmColors.starEmpty
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
            color = if (passed) MaterialTheme.pmColors.textSecondary else MaterialTheme.pmColors.textLabel
        )
    }
}
