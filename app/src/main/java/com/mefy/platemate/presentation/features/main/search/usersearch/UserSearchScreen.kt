package com.mefy.platemate.presentation.features.main.search.usersearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMUserCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserSearchScreen(
    modifier: Modifier = Modifier,
    state: UserSearchUiState,
    onAction: (UserSearchUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onQueryChange = remember(onAction) { { query: String -> onAction(UserSearchUiAction.SearchQueryChanged(query)) } }
    val onClearRecentClick = remember(onAction) { { onAction(UserSearchUiAction.ClearRecentSearchesClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(innerPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dims.spacing.s8)
        ) {

            PMSearchBar(
                query = state.searchQuery,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dims.spacing.s8),
                placeholder = stringResource(R.string.search_users_placeholder),
                isLoading = state.isSearchLoading
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            if (state.searchQuery.isEmpty() && state.recentSearches.isNotEmpty()) {
                item(key = "recent_searches") {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = dims.spacing.s12)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PMText(
                                text = stringResource(R.string.search_recent),
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            PMText(
                                text = stringResource(R.string.common_clear),
                                fontWeight = FontWeight.Bold,
                                color = colors.primary,
                                modifier = Modifier.debouncedClickable(onClick = onClearRecentClick)
                            )
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(top = dims.spacing.s8),
                            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                        ) {
                            state.recentSearches.forEach { searchItem ->
                                val onRecentClick = remember(onAction, searchItem) {
                                    { onAction(UserSearchUiAction.RecentSearchClicked(searchItem.id, searchItem.username)) }
                                }
                                PMChip(
                                    label = searchItem.username,
                                    selected = false,
                                    onClick = onRecentClick
                                )
                            }
                        }
                    }
                }
            }

            if (state.searchQuery.isNotEmpty()) {
                if (state.isSearchLoading && state.searchResults.isEmpty()) {
                    item(key = "loading") {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(dims.spacing.s16),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PMText(text = stringResource(R.string.common_loading), color = colors.textSecondary)
                        }
                    }
                } else if (state.searchResults.isEmpty()) {
                    item(key = "no_results") {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(dims.spacing.s16),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PMText(text = stringResource(R.string.search_no_results), color = colors.textSecondary)
                        }
                    }
                } else {
                    items(state.searchResults, key = { it.id }) { user ->
                        val onUserClick = remember(onAction, user) {
                            { onAction(UserSearchUiAction.SearchUserClicked(user.id, user.username)) }
                        }
                        PMUserCard(
                            displayName = user.username,
                            modifier = Modifier.padding(vertical = dims.spacing.s4),
                            onClick = onUserClick
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "UserSearchScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserSearchScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserSearchScreen(
            state = UserSearchUiState(
                searchQuery = "",
                recentSearches = listOf(
                    UserSearchItemUiModel(1, "caneryildirim"),
                    UserSearchItemUiModel(2, "fatihcelik")
                )
            ),
            onAction = {}
        )
    }
}

@Preview(name = "UserSearchScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserSearchScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserSearchScreen(
            state = UserSearchUiState(
                searchQuery = "can",
                searchResults = listOf(
                    UserSearchItemUiModel(1, "caneryildirim"),
                    UserSearchItemUiModel(3, "cancan")
                )
            ),
            onAction = {}
        )
    }
}
