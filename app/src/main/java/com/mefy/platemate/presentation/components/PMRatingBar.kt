package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMRatingBar(
    starNumber: Int,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val barShape = RoundedCornerShape(dims.radius.r4)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        PMText(
            text = starNumber.toString(),
            fontWeight = FontWeight.SemiBold,
            color = colors.textTertiary,
            modifier = Modifier.width(dims.spacing.s8),
            textAlign = TextAlign.Center
        )

        PMIcon(
            imageVector = Icons.Filled.Star,
            size = dims.sizing.iconSm,
            tint = colors.star,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(dims.sizing.ratingBarHeight)
                .clip(barShape)
                .background(colors.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                    .height(dims.sizing.ratingBarHeight)
                    .clip(barShape)
                    .background(colors.primary)
            )
        }

        PMText(
            text = "${(percentage * 100).toInt()}%",
            color = colors.textLabel,
            modifier = Modifier.width(dims.spacing.s32),
            textAlign = TextAlign.End
        )
    }
}

@Preview(name = "PMRatingBar", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMRatingBarPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMRatingBar(starNumber = 5, percentage = 0.78f)
            PMRatingBar(starNumber = 4, percentage = 0.14f)
            PMRatingBar(starNumber = 3, percentage = 0.05f)
            PMRatingBar(starNumber = 2, percentage = 0.02f)
            PMRatingBar(starNumber = 1, percentage = 0.01f)
        }
    }
}
