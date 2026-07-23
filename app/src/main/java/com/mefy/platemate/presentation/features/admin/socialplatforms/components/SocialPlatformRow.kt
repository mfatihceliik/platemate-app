package com.mefy.platemate.presentation.features.admin.socialplatforms.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.socialplatforms.SocialPlatformListItem
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun SocialPlatformRow(
    item: SocialPlatformListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

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
            PMText(text = item.labels["en"].orEmpty().ifBlank { item.labels["tr"].orEmpty() }, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = "${item.code} • #${item.sortOrder}", style = PMTextStyle.Note, color = colors.textLabel)
        }
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}

@Preview(name = "SocialPlatformRow Active", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SocialPlatformRowActivePreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialPlatformRow(
            item = SocialPlatformListItem(
                id = 1L,
                code = "INSTAGRAM",
                labels = mapOf("tr" to "Instagram", "en" to "Instagram"),
                sortOrder = 1,
                active = true
            ),
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
            item = SocialPlatformListItem(
                id = 2L,
                code = "SNAPCHAT",
                labels = mapOf("tr" to "Snapchat", "en" to "Snapchat"),
                sortOrder = 6,
                active = false
            ),
            onClick = {},
            onToggle = {}
        )
    }
}
