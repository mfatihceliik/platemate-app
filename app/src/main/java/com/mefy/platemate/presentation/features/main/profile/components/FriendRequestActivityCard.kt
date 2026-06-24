package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.model.FriendRequestNotificationItem
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun FriendRequestActivityCard(
    item: FriendRequestNotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = stringResource(R.string.profile_friend_request_title, item.username),
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = colors.textLabel,
                    modifier = Modifier.size(dims.spacing.s16)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = item.statusCode,
                    fontSize = dims.fontSize.sm,
                    color = colors.primary
                )
                PMText(
                    text = item.createdAtText,
                    fontSize = dims.fontSize.sm,
                    color = colors.textLabel
                )
            }
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
            statusCode = "PENDING",
            createdAtText = "2026-05-26",
            sortKey = "2026-05-26T09:30:00Z"
        ),
        onClick = {},
        modifier = Modifier.padding(dims.spacing.s16)
    )
}