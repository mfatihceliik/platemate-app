package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMStatPill
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun UserProfileHeaderCard(
    modifier: Modifier = Modifier,
    displayName: String,
    username: String,
    bio: String,
    isVerified: Boolean,
    isOnline: Boolean,
    reviewCount: Int,
    followerCount: String,
    followingCount: Int,
    onFollowersClick: (() -> Unit)? = null,
    onFollowingClick: (() -> Unit)? = null
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val colors = PMTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMAvatar(
                displayName = displayName,
                size = sizing.avatarXl,
                showOnlineStatus = true,
                isOnline = isOnline
            )

            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.s4)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = displayName,
                        fontSize = fontSize.lg,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    if (isVerified) {
                        PMIcon(
                            imageVector = Icons.Filled.Verified,
                            tint = colors.primary,
                            size = sizing.iconSm,
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMText(
                        text = username,
                        fontSize = fontSize.sm,
                        color = colors.textSecondary
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(spacing.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PMStatPill(
                        value = reviewCount.toString(),
                        label = stringResource(R.string.user_profile_stat_reviews),
                        modifier = Modifier.weight(1f)
                    )
                    PMStatPill(
                        value = followerCount,
                        label = stringResource(R.string.user_profile_stat_followers),
                        onClick = onFollowersClick,
                        modifier = Modifier.weight(1f)
                    )
                    PMStatPill(
                        value = followingCount.toString(),
                        label = stringResource(R.string.user_profile_stat_following),
                        onClick = onFollowingClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (bio.isNotBlank()) {
            PMText(
                text = bio,
                fontSize = fontSize.md,
                color = colors.textPrimary,
                modifier = Modifier.padding(top = spacing.s8, start = spacing.s4)
            )
        }
    }
}

@Preview(name = "UserProfileHeaderCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileHeaderCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserProfileHeaderCard(
            displayName = "Ahmet Yılmaz",
            username = "@ahmetyilmaz",
            bio = "İstanbul sürücüsü. Saygılı ve temkinli araç kullanırım.",
            isVerified = true,
            isOnline = true,
            reviewCount = 47,
            followerCount = "1.2K",
            followingCount = 89
        )
    }
}

@Preview(name = "UserProfileHeaderCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileHeaderCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserProfileHeaderCard(
            displayName = "Ahmet Yılmaz",
            username = "@ahmetyilmaz",
            bio = "İstanbul sürücüsü. Saygılı ve temkinli araç kullanırım.",
            isVerified = true,
            isOnline = false,
            reviewCount = 47,
            followerCount = "1.2K",
            followingCount = 89
        )
    }
}

