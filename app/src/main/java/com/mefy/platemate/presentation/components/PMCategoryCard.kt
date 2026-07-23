package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

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
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize
    val shape = PMTheme.shapes.medium

    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(spacing.s16),
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        Box(
            modifier = Modifier
                //.size(dims.sizing.categoryIconContainer)
                .shadow(elevation = spacing.s0, shape)
                .clip(shape)
                .background(colors.background)
                .padding(spacing.s12)
        ) {
            Box(
                modifier = Modifier
                    .size(sizing.categoryIconDot)
                    .clip(shape)
                    .background(iconColor)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.s4)
        ) {
            PMText(
                text = title,
                color = foregroundColor,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.lg
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
        val spacing = PMTheme.spacing
        val colors = PMTheme.colors
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.s16),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
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
