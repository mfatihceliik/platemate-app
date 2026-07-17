package com.mefy.platemate.presentation.features.main.settings.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.pmRowPositionOf
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    state: LanguageUiState,
    onAction: (LanguageUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onQueryChange = remember(onAction) { { q: String -> onAction(LanguageUiAction.SearchQueryChanged(q)) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PMSearchBar(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            placeholder = stringResource(R.string.profile_language_search),
            modifier = Modifier.fillMaxWidth()
        )

        // Flush group: rows sit gap-less so their border-overlap reads as one card.
        Column {
            state.filteredLanguages.forEachIndexed { index, item ->
                val isSelected = item.language == state.selectedLanguage
                PMRowItem(
                    title = item.displayName,
                    subtitle = item.nativeName,
                    showChevron = false,
                    position = pmRowPositionOf(index, state.filteredLanguages.size),
                    onClick = { item.language?.let { onAction(LanguageUiAction.LanguageSelected(it)) } },
                    trailing = if (isSelected) {
                        {
                            PMIcon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                size = dims.sizing.iconLg,
                                tint = colors.primary
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguagePreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        LanguageScreen(
            state = LanguageUiState(),
            onAction = {},
        )
    }
}
