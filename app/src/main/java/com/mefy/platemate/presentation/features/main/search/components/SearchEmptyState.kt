package com.mefy.platemate.presentation.features.main.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIconContainer
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.search.SearchScreen
import com.mefy.platemate.presentation.features.main.search.SearchUiState
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun SearchEmptyState() {
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        PMIconContainer(
            imageVector = Icons.Outlined.Search,
            iconSize = sizing.iconLg,
            containerSize = sizing.iconXl
        )

        PMText(
            text = stringResource(R.string.search_empty_title),
            fontSize = fontSize.sm,
            color = colors.textPrimary
        )
        PMText(
            text = stringResource(R.string.search_empty_subtitle),
            fontSize = fontSize.sm,
            color = colors.textTertiary
        )
    }
}

@Preview(name = "Search Screen Empty", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SearchScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SearchScreen(
            state = SearchUiState(),
            onAction = {},
            onNavigateToCameraScanner = {}
        )
    }
}