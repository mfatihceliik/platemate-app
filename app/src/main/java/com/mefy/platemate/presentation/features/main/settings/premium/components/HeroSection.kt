package com.mefy.platemate.presentation.features.main.settings.premium.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun HeroSection(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.avatarXl)
                .clip(RoundedCornerShape(dims.radius.r18))
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Filled.Star,
                size = dims.sizing.avatarMd
            )
        }
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