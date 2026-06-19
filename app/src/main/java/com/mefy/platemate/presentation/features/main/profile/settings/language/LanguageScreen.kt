package com.mefy.platemate.presentation.features.main.profile.settings.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMRowDivider
import com.mefy.platemate.presentation.components.PMRowGroup
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    state: LanguageUiState,
    onAction: (LanguageUiAction) -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_language_title),
            onBackClick = { onAction(LanguageUiAction.BackClicked) }
        ),
        containerColor = colors.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            item(key = "search") {
                PMSearchBar(
                    query = state.searchQuery,
                    onQueryChange = { onAction(LanguageUiAction.SearchQueryChanged(it)) },
                    placeholder = stringResource(R.string.profile_language_search),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(key = "language_group") {
                PMRowGroup {
                    state.filteredLanguages.forEachIndexed { index, item ->
                        val isSelected = item.language == state.selectedLanguage
                        PMRowItem(
                            title = item.displayName,
                            subtitle = item.nativeName,
                            showChevron = false,
                            showCard = false,
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
                        if (index < state.filteredLanguages.lastIndex) {
                            PMRowDivider()
                        }
                    }
                }
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
            onAction = {}
        )
    }
}
