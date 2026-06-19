package com.mefy.platemate.presentation.features.main.profile.settings.themecolor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions


@Composable
internal fun ThemePreviewCard(modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, MaterialTheme.shapes.large)
            .border(dims.stroke.st1, colors.outlineVariant, MaterialTheme.shapes.large)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PreviewPlateRow()
        PreviewBadgeRow()
    }
}

@Composable
private fun PreviewPlateRow() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(dims.radius.r12))
                .background(colors.primaryContainer)
                .border(dims.stroke.st1, colors.primaryContainerBorder, RoundedCornerShape(dims.radius.r12)),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = "34",
                style = PMTextStyle.Body,
                fontWeight = FontWeight.ExtraBold,
                color = colors.onPrimaryContainer
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
            PMText(
                text = "34 EK 0682",
                style = PMTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            PMText(
                text = "İstanbul · 4.8 ★",
                style = PMTextStyle.Note,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun PreviewBadgeRow() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val accentColor = colors.primary
    val onAccent = colors.onPrimary

    Row(horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
        PMText(
            text = "Birincil",
            style = PMTextStyle.Caption,
            fontWeight = FontWeight.SemiBold,
            color = onAccent,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(accentColor)
                .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
        )
        PMText(
            text = "Etiket",
            style = PMTextStyle.Caption,
            fontWeight = FontWeight.SemiBold,
            color = colors.onPrimaryContainer,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(colors.primaryContainer)
                .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
        )
    }
}
