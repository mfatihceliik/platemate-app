package com.mefy.platemate.presentation.features.main.platedetail.review.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMRatingStars
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun OverallRatingSection(
    rating: Int,
    label: String,
    onRatingChange: (Int) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8),
        modifier = Modifier.fillMaxWidth()
    ) {
        PMRatingStars(
            rating = rating,
            starSize = dims.sizing.iconXl,
            interactive = true,
            onRatingChange = onRatingChange
        )
        if (label.isNotBlank()) {
            PMText(
                text = label,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
        }
    }
}

@Preview(name = "OverallRatingSection Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun OverallRatingSectionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        OverallRatingSection(rating = 4, label = "Çok iyi", onRatingChange = {})
    }
}

@Preview(name = "OverallRatingSection Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun OverallRatingSectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        OverallRatingSection(rating = 4, label = "Çok iyi", onRatingChange = {})
    }
}