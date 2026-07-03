package com.mefy.platemate.presentation.features.admin.socialplatforms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.socialplatforms.SocialPlatformListItem
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SocialPlatformRow(
    item: SocialPlatformListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
            PMText(text = item.label, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = "${item.code} · #${item.sortOrder}", style = PMTextStyle.Note, color = colors.textLabel)
        }
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}

@Preview(name = "SocialPlatformRow Active", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SocialPlatformRowActivePreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformRow(
            item = SocialPlatformListItem(id = 1L, code = "INSTAGRAM", label = "Instagram", sortOrder = 1, active = true),
            onClick = {},
            onToggle = {}
        )
    }
}

@Preview(name = "SocialPlatformRow Inactive", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SocialPlatformRowInactivePreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SocialPlatformRow(
            item = SocialPlatformListItem(id = 2L, code = "SNAPCHAT", label = "Snapchat", sortOrder = 6, active = false),
            onClick = {},
            onToggle = {}
        )
    }
}
