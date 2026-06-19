package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProBadge() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(dims.radius.r6))
            .background(colors.warning)
            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s4)
    ) {
        PMText(
            text = "PRO",
            style = PMTextStyle.Note,
            fontWeight = FontWeight.Bold,
            color = colors.onPrimary
        )
    }
}