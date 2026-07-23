package com.mefy.platemate.presentation.features.main.search.usersearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMSearchBar
import com.mefy.platemate.presentation.components.PMUserCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun UserSearchScreen(
    modifier: Modifier = Modifier,
    state: UserSearchUiState,
    onAction: (UserSearchUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

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
                .padding(bottom = spacing.s8)
        ) {

            PMSearchBar(
                query = state.searchQuery,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = spacing.s8),
                placeholder = stringResource(R.string.search_users_placeholder),
                isLoading = state.isSearchLoading
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            if (state.searchQuery.isEmpty() && state.recentSearches.isNotEmpty()) {
                item(key = "recent_searches_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = spacing.s12),
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
                }

                items(state.recentSearches, key = { "recent_${it.id}" }) { searchItem ->
                    val onRecentClick = remember(onAction, searchItem) {
                        {
                            onAction(
                                UserSearchUiAction.RecentSearchClicked(
                                    searchItem.id,
                                    searchItem.username,
                                    searchItem.displayName,
                                    searchItem.bio
                                )
                            )
                        }
                    }
                    val onRemoveClick = remember(onAction, searchItem) {
                        { onAction(UserSearchUiAction.RemoveRecentSearchClicked(searchItem.id)) }
                    }
                    PMUserCard(
                        displayName = searchItem.displayName ?: searchItem.username,
                        username = if (!searchItem.displayName.isNullOrBlank()) "@${searchItem.username}" else null,
                        subtitle = searchItem.bio,
                        modifier = Modifier.padding(vertical = spacing.s4),
                        onClick = onRecentClick,
                        trailing = {
                            PMIconButton(
                                imageVector = Icons.Filled.Close,
                                onClick = onRemoveClick,
                                size = sizing.iconSm,
                                iconColor = colors.iconDanger,
                                contentDescription = stringResource(R.string.search_recent_remove)
                            )
                        }
                    )
                }
            }

            if (state.searchQuery.isNotEmpty()) {
                if (state.isSearchLoading && state.searchResults.isEmpty()) {
                    item(key = "loading") {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(spacing.s16),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PMText(text = stringResource(R.string.common_loading), color = colors.textSecondary)
                        }
                    }
                } else if (state.searchResults.isEmpty()) {
                    item(key = "no_results") {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(spacing.s16),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PMText(text = stringResource(R.string.search_no_results), color = colors.textSecondary)
                        }
                    }
                } else {
                    items(state.searchResults, key = { it.id }) { user ->
                        val onUserClick = remember(onAction, user) {
                            {
                                onAction(
                                    UserSearchUiAction.SearchUserClicked(
                                        user.id,
                                        user.username,
                                        user.displayName,
                                        user.bio
                                    )
                                )
                            }
                        }
                        PMUserCard(
                            displayName = user.displayName ?: user.username,
                            username = if (!user.displayName.isNullOrBlank()) "@${user.username}" else null,
                            subtitle = user.bio,
                            modifier = Modifier.padding(vertical = spacing.s4),
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
                    UserSearchItemUiModel(1, "caneryildirim", displayName = "Caner Yıldırım", bio = "İstanbul • Sürücü"),
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
                    UserSearchItemUiModel(1, "caneryildirim", displayName = "Caner Yıldırım", bio = "İstanbul • Sürücü"),
                    UserSearchItemUiModel(3, "cancan")
                )
            ),
            onAction = {}
        )
    }
}
