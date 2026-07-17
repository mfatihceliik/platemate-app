package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMAvatar
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

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
    followingCount: Int
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s16)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dims.spacing.s4),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s16),
                verticalAlignment = Alignment.CenterVertically
            ) {

                PMAvatar(
                    displayName = displayName,
                    size = dims.sizing.avatarXl,
                    showOnlineStatus = true,
                    isOnline = isOnline
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                    ) {
                        PMText(
                            text = displayName,
                            fontSize = dims.fontSize.xl,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        if (isVerified) {
                            PMIcon(
                                imageVector = Icons.Filled.Verified,
                                tint = colors.primary,
                                size = dims.sizing.iconSm,
                            )
                        }
                    }
                    PMText(
                        text = username,
                        fontSize = dims.fontSize.md,
                        color = colors.textSecondary
                    )
                }
            }

            if (bio.isNotBlank()) {
                PMText(
                    text = bio,
                    fontSize = dims.fontSize.md,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = dims.spacing.s12)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dims.spacing.s16, bottom = dims.spacing.s16)
                    .height(IntrinsicSize.Min)
                    .clip(MaterialTheme.shapes.large)
                    .border(dims.stroke.st1, colors.outlineVariant.copy(alpha=0.5f), MaterialTheme.shapes.large)
            ) {
                StatCell(value = reviewCount.toString(), label = stringResource(R.string.user_profile_stat_reviews), modifier = Modifier.weight(1f))
                Box(modifier = Modifier.width(dims.stroke.st1).fillMaxHeight().background(colors.outlineVariant.copy(alpha=0.5f)))
                StatCell(value = followerCount, label = stringResource(R.string.user_profile_stat_followers), modifier = Modifier.weight(1f))
                Box(modifier = Modifier.width(dims.stroke.st1).fillMaxHeight().background(colors.outlineVariant.copy(alpha=0.5f)))
                StatCell(value = followingCount.toString(), label = stringResource(R.string.user_profile_stat_following), modifier = Modifier.weight(1f))
            }
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

@Composable
private fun StatCell(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pmColors
    Column(
        modifier = modifier
            .background(colors.surfaceSecondary)
            .padding(vertical = 11.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        PMText(text = value, style = PMTextStyle.Title, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        PMText(text = label, style = PMTextStyle.Note, color = colors.textTertiary)
    }
}

