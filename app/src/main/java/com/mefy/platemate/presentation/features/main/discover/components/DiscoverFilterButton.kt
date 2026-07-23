package com.mefy.platemate.presentation.features.main.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun DiscoverFilterButton(
    activeCount: Int,
    onClick: () -> Unit
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val shape = PMTheme.shapes

    Box {
        PMIconButton(
            imageVector = Icons.Filled.FilterList,
            onClick = onClick,
            iconColor = if (activeCount > 0) colors.primary else null,
            contentDescription = stringResource(R.string.discover_filter_button_description)
        )
        if (activeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(sizing.iconSm)
                    .clip(shape.medium)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = activeCount.toString(),
                    fontSize = fontSize.xs,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiscoverFilterButtonLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PMTheme.spacing.s16),
            modifier = Modifier.padding(PMTheme.spacing.s16)
        ) {
            DiscoverFilterButton(activeCount = 0, onClick = {})
            DiscoverFilterButton(activeCount = 3, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiscoverFilterButtonDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PMTheme.spacing.s16),
            modifier = Modifier.padding(PMTheme.spacing.s16)
        ) {
            DiscoverFilterButton(activeCount = 0, onClick = {})
            DiscoverFilterButton(activeCount = 3, onClick = {})
        }
    }
}