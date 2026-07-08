package com.mefy.platemate.presentation.features.main.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.search.SearchScreen
import com.mefy.platemate.presentation.features.main.search.SearchUiState
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun SearchEmptyState() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dims.spacing.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Outlined.Search,
            )
        }

        PMText(
            text = stringResource(R.string.search_empty_title),
            fontSize = dims.fontSize.sm,
            color = colors.textPrimary
        )
        PMText(
            text = stringResource(R.string.search_empty_subtitle),
            fontSize = dims.fontSize.sm,
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