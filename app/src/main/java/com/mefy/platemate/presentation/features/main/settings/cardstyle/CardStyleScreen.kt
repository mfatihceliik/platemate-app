package com.mefy.platemate.presentation.features.main.settings.cardstyle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun CardStyleScreen(
    modifier: Modifier = Modifier,
    state: CardStyleUiState,
    onAction: (CardStyleUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val fontSize = PMTheme.fontSize
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes

    val onPremiumCta = remember(onAction) { { onAction(CardStyleUiAction.PremiumCtaClicked) } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(spacing.s16)
    ) {
        item {
            PMText(
                text = stringResource(R.string.card_style_subtitle),
                fontSize = fontSize.md,
                color = colors.textSecondary
            )
        }

        if (!state.isPremium) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shapes.medium)
                        .background(colors.primaryContainer)
                        .debouncedClickable(onClick = onPremiumCta)
                        .padding(spacing.s12),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    PMIcon(
                        imageVector = Icons.Filled.Lock,
                        size = sizing.iconSm,
                        tint = colors.primary
                    )
                    PMText(
                        text = stringResource(R.string.card_style_premium_cta),
                        fontSize = fontSize.md,
                        color = colors.primary
                    )
                }
            }
        }

        items(count = STYLE_OPTIONS.size, key = { STYLE_OPTIONS[it].first.name }) { index ->
            val (style, labelRes) = STYLE_OPTIONS[index]
            StyleOption(
                style = style,
                label = stringResource(labelRes),
                isSelected = state.selectedStyle == style,
                onSelect = { onAction(CardStyleUiAction.StyleSelected(style)) }
            )
        }
    }
}

@Composable
private fun StyleOption(
    style: PMPlateCardStyle,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val stroke = PMTheme.stroke
    val fontSize = PMTheme.fontSize
    val shapes = PMTheme.shapes

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .border(
                width = if (isSelected) stroke.st2 else stroke.st1,
                color = if (isSelected) colors.primary else colors.outlineVariant,
                shape = shapes.medium
            )
            .debouncedClickable(onClick = onSelect)
            .padding(spacing.s12),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = label,
                fontSize = fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                PMIcon(
                    imageVector = Icons.Filled.CheckCircle,
                    size = sizing.iconSm,
                    tint = colors.primary
                )
            }
        }

        PMPlateCard(
            id = "preview_${style.name}",
            rank = 1,
            plateNumber = "34 EK 0682",
            rating = "4.8",
            commentCount = 12,
            searchCount = 1240,
            onClick = { onSelect() },
            style = style,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private val STYLE_OPTIONS = listOf(
    PMPlateCardStyle.Classic to R.string.card_style_classic,
    PMPlateCardStyle.Spotlight to R.string.card_style_spotlight,
    PMPlateCardStyle.Minimal to R.string.card_style_minimal
)

@Preview(name = "CardStyle Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun CardStyleLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CardStyleScreen(
            state = CardStyleUiState(selectedStyle = PMPlateCardStyle.Spotlight, isPremium = true),
            onAction = {}
        )
    }
}

@Preview(name = "CardStyle Locked", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun CardStyleLockedPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        CardStyleScreen(
            state = CardStyleUiState(selectedStyle = PMPlateCardStyle.Classic, isPremium = false),
            onAction = {}
        )
    }
}
