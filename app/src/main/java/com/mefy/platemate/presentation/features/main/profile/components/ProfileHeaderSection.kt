package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMChip
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.components.PMBadge
import com.mefy.platemate.presentation.components.PMIconButton

@Composable
internal fun ProfileHeaderSection(
    header: ProfileHeaderUiModel,
    accountSummary: ProfileAccountSummaryUiModel,
    onFriendsClick: () -> Unit,
    pendingFriendRequestCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.r10)

    val emailText = accountSummary.email
        .takeIf { it.isNotBlank() }
        ?: stringResource(R.string.profile_email_not_specified)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = verticalGradient(
                    colors = listOf(
                        colors.primary.copy(alpha = 0.15f),
                        colors.surface
                    )
                ),
                shape = shape
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            PMAvatar(
                displayName = header.username,
                size = dims.sizing.avatarLg,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = header.username,
                    fontSize = dims.fontSize.xl,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                PMText(
                    text = emailText,
                    fontSize = dims.fontSize.md,
                    color = colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (accountSummary.isPremiumActive) {
                    val until = accountSummary.premiumUntilText
                    val premiumText = if (until == null || until == "-") {
                        stringResource(R.string.profile_premium_active)
                    } else {
                        stringResource(R.string.profile_premium_until_format, until)
                    }
                    PMChip(
                        label = premiumText,
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.iconStar
                    )
                } else {
                    PMChip(
                        label = stringResource(R.string.profile_premium_inactive),
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.textSecondary
                    )
                }

                if (accountSummary.joinedAtText.isNotBlank() && accountSummary.joinedAtText != "-") {
                    PMChip(
                        label = accountSummary.joinedAtText,
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.textSecondary
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dims.spacing.s12)
        ) {
            PMIconButton(
                imageVector = Icons.Filled.Person,
                iconColor = colors.primary,
                size = dims.sizing.iconMd,
                onClick = onFriendsClick
            )
            if (pendingFriendRequestCount > 0) {
                PMBadge(
                    showCount = false,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = dims.spacing.s4, y = -dims.spacing.s4)
                )
            }
        }
    }
}

@Preview(name = "ProfileHeader Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileHeaderSectionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileHeaderSectionPreviewContent()
    }
}

@Preview(name = "ProfileHeader Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileHeaderSectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileHeaderSectionPreviewContent()
    }
}

@Composable
private fun ProfileHeaderSectionPreviewContent() {
    ProfileHeaderSection(
        header = ProfileHeaderUiModel(username = "Caner Yıldırım"),
        accountSummary = ProfileAccountSummaryUiModel(
            email = "caner@platemate.com",
            joinedAtText = "Ocak 2025",
            premiumUntilText = "2026-06-30",
            isPremiumActive = true
        ),
        onFriendsClick = {},
        pendingFriendRequestCount = 1
    )
}