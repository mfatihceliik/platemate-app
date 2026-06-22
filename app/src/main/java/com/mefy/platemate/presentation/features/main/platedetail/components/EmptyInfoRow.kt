package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun EmptyInfoRow(icon: ImageVector, text: String) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Row(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = dims.spacing.s24)
    ) {
        PMIcon(
            imageVector = icon,
        )
        PMText(
            text = text,
            color = colors.textSecondary
        )
    }
}

@Preview(name = "EmptyInfoRow Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EmptyInfoRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        EmptyInfoRow(icon = Icons.Filled.Star, text = "Sürüşünü puanla")
    }
}

@Preview(name = "EmptyInfoRow Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun EmptyInfoRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        EmptyInfoRow(icon = Icons.Filled.Star, text = "Sürüşünü puanla")
    }
}