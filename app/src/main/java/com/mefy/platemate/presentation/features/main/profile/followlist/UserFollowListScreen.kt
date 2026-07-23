package com.mefy.platemate.presentation.features.main.profile.followlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMChipStyle
import com.mefy.platemate.presentation.components.PMEmptyState
import com.mefy.platemate.presentation.components.PMTabItem
import com.mefy.platemate.presentation.components.PMTabRow
import com.mefy.platemate.presentation.components.PMUserCard
import com.mefy.platemate.presentation.theme.PMSpacing
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun UserFollowListScreen(
    state: UserFollowListUiState,
    onAction: (UserFollowListUiAction) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + spacing.s16
        ),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        item {
            PMTabRow(
                selectedTabIndex = state.selectedTab,
                tabs = listOf(
                    PMTabItem(title = stringResource(R.string.user_follow_list_followers_tab)),
                    PMTabItem(title = stringResource(R.string.user_follow_list_following_tab))
                ),
                onTabSelected = { onAction(UserFollowListUiAction.TabChanged(it)) }
            )
        }

        if (state.selectedTab == 0) {
            if (state.followers.isEmpty()) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.PeopleOutline,
                        message = stringResource(R.string.user_follow_list_empty_followers)
                    )
                }
            } else {
                items(state.followers, key = { it.userId }) { user ->
                    FollowListRow(user = user, onAction = onAction, spacing = spacing)
                }
            }
        } else {
            if (state.isFollowingListHidden) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.Lock,
                        message = stringResource(R.string.user_follow_list_hidden_following)
                    )
                }
            } else if (state.following.isEmpty()) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.PeopleOutline,
                        message = stringResource(R.string.user_follow_list_empty_following)
                    )
                }
            } else {
                items(state.following, key = { it.userId }) { user ->
                    FollowListRow(user = user, onAction = onAction, spacing = spacing)
                }
            }
        }
    }
}

@Composable
private fun FollowListRow(
    user: FollowListItemUiModel,
    onAction: (UserFollowListUiAction) -> Unit,
    spacing: PMSpacing
) {
    val colors = PMTheme.colors
    PMUserCard(
        displayName = user.displayName,
        username = user.username,
        subtitle = user.bio,
        onClick = { onAction(UserFollowListUiAction.UserClicked(user.userId.toString())) },
        modifier = Modifier.padding(horizontal = spacing.s16),
        trailing = {
            PMChip(
                label = stringResource(
                    if (user.isFollowing) R.string.user_profile_following_label else R.string.user_profile_follow
                ),
                style = if (user.isFollowing) PMChipStyle.Outline else PMChipStyle.Solid,
                accentColor = colors.primary,
                onClick = {
                    onAction(UserFollowListUiAction.FollowToggleClicked(user.userId, user.isFollowing))
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun UserFollowListScreenPreview() {
    PlateMateTheme {
        UserFollowListScreen(
            state = UserFollowListUiState(
                isLoading = false,
                following = listOf(
                    FollowListItemUiModel(1, "Ahmet Yılmaz", "@ahmety", "İstanbul", true),
                    FollowListItemUiModel(2, "Mehmet Öz", "@mehmeto", "Ankara", false)
                )
            ),
            onAction = {}
        )
    }
}
