package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ProfileBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(dims.radius.rFull))
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
    ) {
        PMText(
            text = text,
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview(name = "ProfileBadge Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ProfileBadgeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileBadgePreviewContent()
    }
}

@Preview(name = "ProfileBadge Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileBadgeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileBadgePreviewContent()
    }
}

@Composable
private fun ProfileBadgePreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    Row(
        modifier = Modifier.padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        ProfileBadge(
            text = "Premium",
            backgroundColor = colors.primary,
            textColor = colors.onPrimary
        )
        ProfileBadge(
            text = "Ocak 2025",
            backgroundColor = colors.surfaceVariant,
            textColor = colors.textSecondary
        )
    }
}