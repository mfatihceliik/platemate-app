package com.mefy.platemate.presentation.features.admin.plateremovalreasons.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.plateremovalreasons.PlateRemovalReasonListItem
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateRemovalReasonRow(
    item: PlateRemovalReasonListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    val note = if (item.requiresDescription) {
        "${item.code} · ${stringResource(R.string.admin_plate_removal_reason_requires_description_badge)}"
    } else {
        item.code
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
            PMText(text = item.label, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = note, style = PMTextStyle.Note, color = colors.textLabel)
        }
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}

@Preview(name = "PlateRemovalReasonRow Active", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PlateRemovalReasonRowActivePreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalReasonRow(
            item = PlateRemovalReasonListItem(
                id = 7L,
                code = "OTHER",
                label = "Diğer",
                requiresDescription = true,
                active = true
            ),
            onClick = {},
            onToggle = {}
        )
    }
}

@Preview(name = "PlateRemovalReasonRow Inactive", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateRemovalReasonRowInactivePreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateRemovalReasonRow(
            item = PlateRemovalReasonListItem(
                id = 6L,
                code = "SPAM",
                label = "Spam",
                requiresDescription = false,
                active = false
            ),
            onClick = {},
            onToggle = {}
        )
    }
}

