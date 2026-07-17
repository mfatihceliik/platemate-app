package com.mefy.platemate.presentation.components

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMStatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    deltaText: String? = null,
    deltaPositive: Boolean? = null,
    onClick: (() -> Unit)? = null
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier,
        onClick = onClick,
        padding = PaddingValues(
            vertical = dims.spacing.s16,
            horizontal = dims.spacing.s8
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = value,
                fontSize = dims.fontSize.xxl,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            PMText(
                text = label,
                fontSize = dims.fontSize.md,
                color = colors.textSecondary
            )
            if (deltaText != null) {
                PMText(
                    text = deltaText,
                    fontSize = dims.fontSize.sm,
                    fontWeight = FontWeight.Medium,
                    color = if (deltaPositive == true) colors.success else colors.error
                )
            }
        }
    }
}

@Preview(name = "PMStatCard", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMStatCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMStatCard(value = "42", label = "Degerlendirme", modifier = Modifier.weight(1f), deltaText = "+12%", deltaPositive = true)
            PMStatCard(value = "318", label = "Takipci", modifier = Modifier.weight(1f), deltaText = "-4%", deltaPositive = false)
            PMStatCard(value = "4.6", label = "Puan", modifier = Modifier.weight(1f))
        }
    }
}
