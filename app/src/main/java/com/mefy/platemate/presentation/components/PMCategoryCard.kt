package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMCategoryCard(
    title: String,
    count: String,
    backgroundColor: Color,
    foregroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val pmColors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.r16)

    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(
            modifier = Modifier
                //.size(dims.sizing.categoryIconContainer)
                .shadow(elevation = dims.spacing.s0, RoundedCornerShape(dims.radius.r12))
                .clip(RoundedCornerShape(dims.radius.r12))
                .background(pmColors.background)
                .padding(dims.spacing.s12)
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.categoryIconDot)
                    .clip(RoundedCornerShape(dims.radius.r4))
                    .background(iconColor)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = title,
                color = foregroundColor,
                fontWeight = FontWeight.Bold,
                fontSize = dims.fontSize.lg
            )
            PMText(
                text = count,
            )
        }
    }
}

@Preview(name = "PMCategoryCard", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMCategoryCardPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = MaterialTheme.pmColors
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PMCategoryCard(
                title = "En Nazik",
                count = "214 plaka",
                backgroundColor = colors.categoryTealBg,
                foregroundColor = colors.categoryTealFg,
                iconColor = colors.categoryTealIcon,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            PMCategoryCard(
                title = "Hizli Yanit",
                count = "176 plaka",
                backgroundColor = colors.categoryIndigoBg,
                foregroundColor = colors.categoryIndigoFg,
                iconColor = colors.categoryIndigoIcon,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}
