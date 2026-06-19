package com.mefy.platemate.presentation.features.main.profile.settings.notifications

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMRowDivider
import com.mefy.platemate.presentation.components.PMRowGroup
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.features.main.profile.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun NotificationPreferencesScreen(
    state: NotificationPreferencesUiState,
    onAction: (NotificationPreferencesUiAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_notification_preferences_title),
            onBackClick = onBackClick
        ),
        containerColor = colors.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
            ) {
                PMButton(
                    text = stringResource(R.string.common_save),
                    onClick = { onAction(NotificationPreferencesUiAction.SaveClicked) },
                    enabled = state.hasChanges,
                    loading = state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = state.isLoading,
            label = "notif_prefs_loading"
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = dims.spacing.s16),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
                ) {

                    item(key = "messaging_label") {
                        SectionLabel(text = stringResource(R.string.profile_notif_section_messaging))
                    }

                    item(key = "messaging") {
                        PMRowGroup {
                            PMRowItem(
                                title = stringResource(R.string.profile_notif_messaging_open),
                                subtitle = stringResource(R.string.profile_notif_messaging_open_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.messagingEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.MessagingChanged(it)) }
                                    )
                                }
                            )

                            PMRowDivider()

                            PMRowItem(
                                title = stringResource(R.string.profile_notif_message_alerts),
                                subtitle = stringResource(R.string.profile_notif_message_alerts_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.messageNotificationsEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.MessageNotificationsChanged(it)) }
                                    )
                                }
                            )
                        }
                    }

                    item(key = "social_label") {
                        SectionLabel(text = stringResource(R.string.profile_notif_section_social))
                    }

                    item(key = "social") {
                        PMRowGroup {
                            PMRowItem(
                                title = stringResource(R.string.profile_notif_friend_add),
                                subtitle = stringResource(R.string.profile_notif_friend_add_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.friendNotificationsEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.FriendNotificationsChanged(it)) }
                                    )
                                }
                            )

                            PMRowDivider()

                            PMRowItem(
                                title = stringResource(R.string.profile_notif_new_follower),
                                subtitle = stringResource(R.string.profile_notif_new_follower_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.newFollowerEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.NewFollowerChanged(it)) }
                                    )
                                }
                            )
                        }
                    }

                    item(key = "review_label") {
                        SectionLabel(text = stringResource(R.string.profile_notif_section_review))
                    }

                    item(key = "review") {
                        PMRowGroup {
                            PMRowItem(
                                title = stringResource(R.string.profile_notif_plate_review),
                                subtitle = stringResource(R.string.profile_notif_plate_review_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.plateReviewEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.PlateReviewChanged(it)) }
                                    )
                                }
                            )

                            PMRowDivider()

                            PMRowItem(
                                title = stringResource(R.string.profile_notif_review_reply),
                                subtitle = stringResource(R.string.profile_notif_review_reply_desc),
                                showCard = false,
                                trailing = {
                                    PMSwitch(
                                        checked = state.reviewReplyEnabled,
                                        onCheckedChange = { onAction(NotificationPreferencesUiAction.ReviewReplyChanged(it)) }
                                    )
                                }
                            )
                        }
                    }
                }
            }
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
                reviewReplyEnabled = false
            ),
            onAction = {},
            onBackClick = {}
        )
    }
}
