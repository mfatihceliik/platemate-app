package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.variant.PMCardVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMStatPill(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
    variant: PMCardVariant = PMCardVariant.Standard,
    onClick: (() -> Unit)? = null
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    PMCard(
        modifier = modifier.fillMaxHeight(),
        variant = variant,
        onClick = onClick,
        padding = PaddingValues(horizontal = spacing.s8, vertical = spacing.s12)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s4, Alignment.CenterVertically)
        ) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(spacing.s8)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
            PMText(
                text = value,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                maxLines = 1
            )
            PMText(
                text = label,
                style = PMTextStyle.Note,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(name = "PMStatPill Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMStatPillLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMStatPillPreviewContent()
    }
}

@Preview(name = "PMStatPill Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMStatPillDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMStatPillPreviewContent()
    }
}

@Composable
private fun PMStatPillPreviewContent() {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        PMStatPill(
            value = "124",
            label = "Onaylanan",
            dotColor = colors.success,
            variant = PMCardVariant.Large,
            modifier = Modifier.weight(1f)
        )
        PMStatPill(
            value = "47",
            label = "Değerlendirme",
            modifier = Modifier.weight(1f)
        )
        PMStatPill(
            value = "6",
            label = "Reddedilen",
            dotColor = colors.error,
            variant = PMCardVariant.Large,
            modifier = Modifier.weight(1f)
        )
    }
}
