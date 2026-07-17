package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMCardVariant
import com.mefy.platemate.presentation.components.util.friendRequestStatusStyle
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.FriendRequestStatusUi
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun FriendRequestActivityCard(
    item: FriendRequestNotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        variant = PMCardVariant.Large,
        padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMAvatar(
                displayName = item.username,
                size = dims.sizing.avatarMd,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = stringResource(R.string.profile_friend_request_title, item.username),
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val style = friendRequestStatusStyle(item.status, colors)
                    PMChip(
                        label = stringResource(style.label),
                        containerColor = style.background,
                        accentColor = style.foreground
                    )
                    PMText(
                        text = item.createdAtText,
                        fontSize = dims.fontSize.sm,
                        color = colors.textLabel
                    )
                }
            }

            PMIcon(
                imageVector = Icons.Filled.ChevronRight,
            )
        }
    }
}

@Preview(name = "FriendRequestCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun FriendRequestActivityCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        FriendRequestActivityCardPreviewContent()
    }
}

@Preview(name = "FriendRequestCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun FriendRequestActivityCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        FriendRequestActivityCardPreviewContent()
    }
}

@Composable
private fun FriendRequestActivityCardPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    FriendRequestActivityCard(
        item = FriendRequestNotificationItem(
            id = "friend_1",
            friendUserId = 7,
            username = "fatih",
            status = FriendRequestStatusUi.REQUESTED,
            createdAtText = "2026-05-26",
            sortKey = "2026-05-26T09:30:00Z"
        ),
        onClick = {},
        modifier = Modifier.padding(dims.spacing.s16)
    )
}
