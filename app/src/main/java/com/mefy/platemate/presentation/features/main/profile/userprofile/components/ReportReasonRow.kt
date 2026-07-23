package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ReportReasonRow(
    reason: ReportReason,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val rowShape = PMTheme.shapes.small
    val primary = colors.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(colors.surfaceSecondary)
            .border(
                width = stroke.st2,
                color = if (isSelected) primary else colors.cardBorder,
                shape = rowShape
            )
            .debouncedClickable(onClick = onSelected)
            .padding(spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(spacing.s24)
                .clip(CircleShape)
                .border(stroke.st2, if (isSelected) primary else colors.disabled, CircleShape)
                .background(if (isSelected) primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(spacing.s8)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMText(
                text = stringResource(reason.labelRes),
                style = PMTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
            PMText(
                text = stringResource(reason.descriptionRes),
                style = PMTextStyle.Note,
                color = colors.textTertiary
            )
        }
    }
}

@Preview(name = "ReportReasonRow Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportReasonRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Column(modifier = Modifier.padding(spacing.s16), verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
            ReportReasonRow(reason = ReportReason.SPAM, isSelected = false, onSelected = {})
            ReportReasonRow(reason = ReportReason.HARASSMENT, isSelected = true, onSelected = {})
            ReportReasonRow(reason = ReportReason.INAPPROPRIATE_CONTENT, isSelected = false, onSelected = {})
        }
    }
}

@Preview(name = "ReportReasonRow Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReportReasonRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Column(modifier = Modifier.padding(spacing.s16), verticalArrangement = Arrangement.spacedBy(spacing.s8)) {
            ReportReasonRow(reason = ReportReason.SPAM, isSelected = false, onSelected = {})
            ReportReasonRow(reason = ReportReason.HARASSMENT, isSelected = true, onSelected = {})
            ReportReasonRow(reason = ReportReason.INAPPROPRIATE_CONTENT, isSelected = false, onSelected = {})
        }
    }
}
