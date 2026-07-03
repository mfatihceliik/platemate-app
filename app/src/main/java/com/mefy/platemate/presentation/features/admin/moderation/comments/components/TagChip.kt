package com.mefy.platemate.presentation.features.admin.moderation.comments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun TagChip(text: String) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    PMText(
        text = text,
        style = PMTextStyle.Note,
        color = colors.primary,
        modifier = Modifier
            .background(colors.primaryContainer, RoundedCornerShape(dims.radius.r8))
            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s4)
    )
}

@Preview(name = "TagChip Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TagChipLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        TagChip(text = "Agresif")
    }
}

@Preview(name = "TagChip Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun TagChipDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        TagChip(text = "Agresif")
    }
}