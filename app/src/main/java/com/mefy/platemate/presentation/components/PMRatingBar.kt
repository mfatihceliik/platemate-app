package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val barShape = RoundedCornerShape(4.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Text(
            text = starNumber.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.textTertiary,
            modifier = Modifier.width(8.dp),
            textAlign = TextAlign.Center
        )

        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = colors.star,
            modifier = Modifier.size(11.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(dims.sizing.ratingBarHeight)
                .clip(barShape)
                .background(colors.chipBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                    .height(dims.sizing.ratingBarHeight)
                    .clip(barShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        Text(
            text = "${(percentage * 100).toInt()}%",
            fontSize = 11.sp,
            color = colors.textLabel,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.End
        )
    }
}

@Preview(name = "PMRatingBar", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMRatingBarPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            PMRatingBar(starNumber = 5, percentage = 0.78f)
            PMRatingBar(starNumber = 4, percentage = 0.14f)
            PMRatingBar(starNumber = 3, percentage = 0.05f)
            PMRatingBar(starNumber = 2, percentage = 0.02f)
            PMRatingBar(starNumber = 1, percentage = 0.01f)
        }
    }
}
