package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AppearanceTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Box(
        modifier = modifier
            .height(dims.spacing.s32)
            .clip(RoundedCornerShape(dims.radius.r10))
            .then(
                if (isSelected) Modifier.background(colors.surface)
                else Modifier
            )
            .debouncedClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        PMText(
            text = label,
            style = PMTextStyle.Body,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) colors.textPrimary else colors.textSecondary
        )
    }
}
