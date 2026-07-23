package com.mefy.platemate.presentation.features.main.settings.premium.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIconContainer
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun HeroSection(
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s10)
    ) {

        PMIconContainer(
            imageVector = Icons.Filled.Star,
            tint = colors.primary,
            iconSize = sizing.iconXl,
            containerColor = colors.primaryContainer,
            containerSize = sizing.avatarXl,
        )
        PMText(
            text = stringResource(R.string.profile_premium_headline),
            style = PMTextStyle.Headline,
            fontWeight = FontWeight.ExtraBold,
            color = colors.textPrimary
        )
        PMText(
            text = stringResource(R.string.profile_premium_subtitle),
            style = PMTextStyle.Note,
            color = colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeroSectionPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        HeroSection()
    }
}