package com.mefy.platemate.presentation.features.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AuthHeroHeader(
    badgeText: String, // Kept for API compatibility, not visually used as a badge anymore, instead we just render 'P'
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val spacing = dims.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.surface)
            .padding(top = spacing.s24, bottom = spacing.s48),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(
                    color = colors.primary,
                    shape = RoundedCornerShape(spacing.s24)
                ),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = "P",
                color = Color.White,
                fontSize = dims.fontSize.huge,
                fontWeight = FontWeight.ExtraBold,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMText(
                text = title,
                fontSize = dims.fontSize.xxl,
                fontWeight = FontWeight.ExtraBold,
                color = colors.onSurface
            )
            PMText(
                text = subtitle,
                style = PMTextStyle.Body,
                color = colors.onSurfaceVariant
            )
        }
    }
}
