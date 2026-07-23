package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMRatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = PMTheme.sizing.iconMd,
    interactive: Boolean = false,
    onRatingChange: ((Int) -> Unit)? = null
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.s0)
    ) {
        for (i in 1..maxStars) {
            val filled = i <= rating
            val starModifier = if (interactive && onRatingChange != null) {
                Modifier
                    .size(starSize)
                    .debouncedClickable { onRatingChange(i) }
            } else {
                Modifier.size(starSize)
            }

            PMIcon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (filled) colors.iconStar else colors.iconStarEmpty,
                modifier = starModifier
            )
        }
    }
}

@Preview(name = "PMRatingStars", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMRatingStarsPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            PMRatingStars(rating = 4, starSize = spacing.s32)
            PMRatingStars(rating = 3, starSize = spacing.s12)

            var selectedRating by remember { mutableIntStateOf(0) }
            PMRatingStars(
                rating = selectedRating,
                interactive = true,
                onRatingChange = { selectedRating = it }
            )
        }
    }
}
