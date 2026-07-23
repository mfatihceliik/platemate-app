package com.mefy.platemate.presentation.features.main.settings.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMRowPosition
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun NotificationPreferencesScreen(
    modifier: Modifier = Modifier,
    state: NotificationPreferencesUiState,
    onAction: (NotificationPreferencesUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {

    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    val onSave = remember(onAction) { { onAction(NotificationPreferencesUiAction.SaveClicked) } }
    val onMessagingChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.MessagingChanged(v)) } }
    val onOnlineVisibilityChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.OnlineVisibilityChanged(v)) } }
    val onMessageNotificationsChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.MessageNotificationsChanged(v)) } }
    val onFriendNotificationsChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.FriendNotificationsChanged(v)) } }
    val onNewFollowerChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.NewFollowerChanged(v)) } }
    val onPlateReviewChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.PlateReviewChanged(v)) } }
    val onReviewReplyChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.ReviewReplyChanged(v)) } }
    val onFollowingListVisibleChanged = remember(onAction) { { v: Boolean -> onAction(NotificationPreferencesUiAction.FollowingListVisibleChanged(v)) } }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = spacedByWithFooter(spacing.s16)

    ) {
        item {
            PMSectionLabel(
                text = stringResource(R.string.profile_notif_section_messaging)
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_messaging_open),
                subtitle = stringResource(R.string.profile_notif_messaging_open_desc),
                position = PMRowPosition.Top,
                trailing = {
                    PMSwitch(
                        checked = state.messagingEnabled, onCheckedChange = onMessagingChanged
                    )
                })
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_online_visibility),
                subtitle = stringResource(R.string.profile_notif_online_visibility_desc),
                position = PMRowPosition.Middle,
                trailing = {
                    PMSwitch(
                        checked = state.onlineVisibilityEnabled,
                        onCheckedChange = onOnlineVisibilityChanged
                    )
                })
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_message_alerts),
                subtitle = stringResource(R.string.profile_notif_message_alerts_desc),
                position = PMRowPosition.Bottom,
                trailing = {
                    PMSwitch(
                        checked = state.messageNotificationsEnabled,
                        onCheckedChange = onMessageNotificationsChanged
                    )
                })
        }

        item {
            PMSectionLabel(
                text = stringResource(R.string.profile_notif_section_social)
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_friend_add),
                subtitle = stringResource(R.string.profile_notif_friend_add_desc),
                position = PMRowPosition.Top,
                trailing = {
                    PMSwitch(
                        checked = state.friendNotificationsEnabled,
                        onCheckedChange = onFriendNotificationsChanged
                    )
                })
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_new_follower),
                subtitle = stringResource(R.string.profile_notif_new_follower_desc),
                position = PMRowPosition.Middle,
                trailing = {
                    PMSwitch(
                        checked = state.newFollowerEnabled,
                        onCheckedChange = onNewFollowerChanged
                    )
                })
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_following_list_visible),
                subtitle = stringResource(R.string.profile_notif_following_list_visible_desc),
                position = PMRowPosition.Bottom,
                trailing = {
                    PMSwitch(
                        checked = state.followingListVisible,
                        onCheckedChange = onFollowingListVisibleChanged
                    )
                })
        }

        item {
            PMSectionLabel(
                text = stringResource(R.string.profile_notif_section_review)
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_plate_review),
                subtitle = stringResource(R.string.profile_notif_plate_review_desc),
                position = PMRowPosition.Top,
                trailing = {
                    PMSwitch(
                        checked = state.plateReviewEnabled,
                        onCheckedChange = onPlateReviewChanged
                    )
                })
        }

        item {
            PMRowItem(
                title = stringResource(R.string.profile_notif_review_reply),
                subtitle = stringResource(R.string.profile_notif_review_reply_desc),
                position = PMRowPosition.Bottom,
                trailing = {
                    PMSwitch(
                        checked = state.reviewReplyEnabled,
                        onCheckedChange = onReviewReplyChanged
                    )
                })
        }

        item {
            PMButton(
                text = stringResource(R.string.common_save),
                onClick = onSave,
                enabled = state.hasChanges,
                loading = state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.s16)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotifPreferencesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        NotificationPreferencesScreen(
            state = NotificationPreferencesUiState(
                isLoading = false,
                messagingEnabled = true,
                messageNotificationsEnabled = true,
                friendNotificationsEnabled = true,
                newFollowerEnabled = false,
                plateReviewEnabled = true,
                reviewReplyEnabled = false,
                followingListVisible = false
            ),
            onAction = {},
        )
    }
}
