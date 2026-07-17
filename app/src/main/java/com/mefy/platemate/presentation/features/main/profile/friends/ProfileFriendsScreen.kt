package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMSearchBar
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMUserCard
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMChipStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMTabRow
import com.mefy.platemate.presentation.components.PMTabItem
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.SearchOff
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import androidx.compose.material3.CircularProgressIndicator
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.components.PMEmptyState
import com.mefy.platemate.presentation.components.PMSectionLabel

@Composable
fun ProfileFriendsScreen(
    state: ProfileFriendsUiState,
    onAction: (ProfileFriendsUiAction) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues()
) {
    val colors = MaterialTheme.pmColors
    val spacing = MaterialTheme.pmDimensions.spacing

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = innerPadding.calculateBottomPadding() + spacing.s16
        ),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        item {
            val totalCount = state.pendingRequests.size + state.sentRequests.size
            val requestsLabel = stringResource(R.string.profile_friends_requests_tab).let { label ->
                if (totalCount > 0) "$label ($totalCount)" else label
            }
            PMTabRow(
                selectedTabIndex = state.selectedTab,
                tabs = listOf(
                    PMTabItem(
                        title = stringResource(R.string.user_profile_friends),
                        icon = Icons.Filled.Person
                    ),
                    PMTabItem(
                        title = requestsLabel,
                        icon = Icons.Filled.PersonAdd
                    )
                ),
                onTabSelected = { onAction(ProfileFriendsUiAction.TabChanged(it)) }
            )
        }

        item {
            PMSearchBar(
                query = state.searchQuery,
                onQueryChange = { onAction(ProfileFriendsUiAction.SearchQueryChanged(it)) },
                placeholder = stringResource(R.string.profile_friends_search_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.s16, vertical = spacing.s8)
            )
        }

        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.s32),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        } else if (state.selectedTab == 0) {
            if (state.friends.isEmpty()) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.PeopleOutline,
                        message = stringResource(R.string.profile_friends_empty_no_friends)
                    )
                }
            } else if (state.filteredFriends.isEmpty() && state.friends.isNotEmpty()) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.SearchOff,
                        message = stringResource(R.string.profile_friends_empty_no_results)
                    )
                }
            } else {
                items(state.filteredFriends, key = { it.id }) { friend ->
                    PMUserCard(
                        displayName = friend.username, // Using username as display name
                        username = "@${friend.username}",
                        subtitle = friend.subtitleText,
                        onClick = { onAction(ProfileFriendsUiAction.FriendClicked(friend.friendUserId.toString())) },
                        modifier = Modifier.padding(horizontal = spacing.s16),
                        trailing = {
                            PMChip(
                                label = stringResource(R.string.profile_friends_remove_action),
                                style = PMChipStyle.Outline,
                                accentColor = colors.error,
                                onClick = { onAction(ProfileFriendsUiAction.RemoveFriendClicked(friend.friendUserId)) }
                            )
                        }
                    )
                }
            }
        } else {
            if (state.pendingRequests.isEmpty() && state.sentRequests.isEmpty()) {
                item {
                    PMEmptyState(
                        icon = Icons.Outlined.MailOutline,
                        message = stringResource(R.string.profile_friends_empty_no_pending_requests)
                    )
                }
            } else {
                if (state.pendingRequests.isNotEmpty()) {
                    item {
                        PMSectionLabel(
                            text = "${stringResource(R.string.profile_friends_received_section)} (${state.pendingRequests.size})",
                            modifier = Modifier.padding(horizontal = spacing.s16)
                        )
                    }
                    if (state.filteredPendingRequests.isEmpty()) {
                        item {
                            PMEmptyState(
                                icon = Icons.Outlined.SearchOff,
                                message = stringResource(R.string.profile_friends_empty_no_results)
                            )
                        }
                    } else {
                        items(state.filteredPendingRequests, key = { it.id }) { request ->
                            PMUserCard(
                                displayName = request.username,
                                username = "@${request.username}",
                                subtitle = request.subtitleText,
                                onClick = { onAction(ProfileFriendsUiAction.FriendClicked(request.friendUserId.toString())) },
                                modifier = Modifier.padding(horizontal = spacing.s16),
                                trailing = {
                                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.s8)) {
                                        PMIconButton(
                                            imageVector = Icons.Default.Check,
                                            onClick = { onAction(ProfileFriendsUiAction.AcceptRequestClicked(request.id)) },
                                            variant = PMIconButtonVariant.Filled
                                        )
                                        PMIconButton(
                                            imageVector = Icons.Default.Close,
                                            onClick = { onAction(ProfileFriendsUiAction.RejectRequestClicked(request.id)) },
                                            variant = PMIconButtonVariant.Outlined
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                if (state.sentRequests.isNotEmpty()) {
                    item {
                        PMSectionLabel(
                            text = "${stringResource(R.string.profile_friends_sent_section)} (${state.sentRequests.size})",
                            modifier = Modifier.padding(horizontal = spacing.s16)
                        )
                    }
                    if (state.filteredSentRequests.isEmpty()) {
                        item {
                            PMEmptyState(
                                icon = Icons.Outlined.SearchOff,
                                message = stringResource(R.string.profile_friends_empty_no_results)
                            )
                        }
                    } else {
                        items(state.filteredSentRequests, key = { it.id }) { request ->
                            PMUserCard(
                                displayName = request.username,
                                username = "@${request.username}",
                                subtitle = request.subtitleText,
                                onClick = { onAction(ProfileFriendsUiAction.FriendClicked(request.friendUserId.toString())) },
                                modifier = Modifier.padding(horizontal = spacing.s16),
                                trailing = {
                                    PMChip(
                                        label = stringResource(R.string.profile_friends_cancel_request),
                                        style = PMChipStyle.Outline,
                                        accentColor = colors.error,
                                        onClick = { onAction(ProfileFriendsUiAction.CancelRequestClicked(request.id)) }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showRemoveFriendPopup) {
        PMPopup(
            title = stringResource(R.string.common_confirm),
            message = stringResource(
                if (state.isCancelingSentRequest) {
                    R.string.profile_friends_cancel_request_confirmation
                } else {
                    R.string.user_profile_unfriend_confirmation
                }
            ),
            onDismissRequest = { onAction(ProfileFriendsUiAction.CancelRemoveFriend) },
            primaryText = stringResource(R.string.common_yes),
            onPrimaryClick = { onAction(ProfileFriendsUiAction.ConfirmRemoveFriend) },
            secondaryText = stringResource(R.string.common_cancel),
            onSecondaryClick = { onAction(ProfileFriendsUiAction.CancelRemoveFriend) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileFriendsScreenPreview() {
    PlateMateTheme {
        ProfileFriendsScreen(
            state = ProfileFriendsUiState(
                isLoading = false,
                friends = listOf(
                    ProfileFriendUiModel(1, 100, "ahmet_y", "ACTIVE", "Arkadaşlık tarihi: 2024-01-01"),
                    ProfileFriendUiModel(2, 101, "mehmet_x", "ACTIVE", "Arkadaşlık tarihi: 2024-01-02")
                ),
                filteredFriends = listOf(
                    ProfileFriendUiModel(1, 100, "ahmet_y", "ACTIVE", "Arkadaşlık tarihi: 2024-01-01"),
                    ProfileFriendUiModel(2, 101, "mehmet_x", "ACTIVE", "Arkadaşlık tarihi: 2024-01-02")
                )
            ),
            onAction = {}
        )
    }
}
