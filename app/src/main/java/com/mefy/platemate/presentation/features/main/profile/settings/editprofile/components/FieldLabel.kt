package com.mefy.platemate.presentation.features.main.profile.settings.editprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    PMText(
        text = text,
        style = PMTextStyle.SectionLabel,
        color = MaterialTheme.pmColors.textTertiary,
        modifier = modifier
    )
}

@Preview(name = "FieldLabel Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun FieldLabelLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            FieldLabel("AD SOYAD")
            FieldLabel("KULLANICI ADI")
            FieldLabel("BİYOGRAFİ")
        }
    }
}

@Preview(name = "FieldLabel Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun FieldLabelDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            FieldLabel("AD SOYAD")
            FieldLabel("KULLANICI ADI")
            FieldLabel("BİYOGRAFİ")
        }
    }
}
