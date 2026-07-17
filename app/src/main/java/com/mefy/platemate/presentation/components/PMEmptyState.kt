package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMEmptyState(
    icon: ImageVector,
    message: String,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12, Alignment.CenterVertically)
    ) {
        PMIcon(
            imageVector = icon,
            tint = colors.primary,
            size = dims.sizing.iconXl
        )
        PMText(
            text = message,
            style = PMTextStyle.Body,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "PMEmptyState Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMEmptyStateLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMEmptyState(icon = Icons.Outlined.SearchOff, message = "Sonuç bulunamadı")
    }
}

@Preview(name = "PMEmptyState Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMEmptyStateDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMEmptyState(icon = Icons.Outlined.SearchOff, message = "Sonuç bulunamadı")
    }
}
