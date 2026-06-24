package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileHeaderSection(
    header: ProfileHeaderUiModel,
    accountSummary: ProfileAccountSummaryUiModel,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val emailText = accountSummary.email
        .takeIf { it.isNotBlank() }
        ?: stringResource(R.string.profile_email_not_specified)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(bottomStart = dims.radius.r16, bottomEnd = dims.radius.r16)
            )
            .padding(top = dims.spacing.s32, bottom = dims.spacing.s24, start = dims.spacing.s16, end = dims.spacing.s16)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = dims.spacing.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarLarge)
                    .clip(CircleShape)
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Person,
                    tint = colors.onPrimaryContainer,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = header.username,
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                PMText(
                    text = emailText,
                    fontSize = dims.fontSize.sm,
                    color = colors.textLabel,
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
                    ProfileBadge(
                        text = premiumText,
                        backgroundColor = colors.primary,
                        textColor = colors.onPrimary
                    )
                } else {
                    ProfileBadge(
                        text = stringResource(R.string.profile_premium_inactive),
                        backgroundColor = colors.surfaceVariant,
                        textColor = colors.textSecondary
                    )
                }

                if (accountSummary.joinedAtText.isNotBlank() && accountSummary.joinedAtText != "-") {
                    ProfileBadge(
                        text = accountSummary.joinedAtText,
                        backgroundColor = colors.surfaceVariant,
                        textColor = colors.textSecondary
                    )
                }
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
        )
    )
}