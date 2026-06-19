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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileHeaderCard(
    displayName: String,
    username: String,
    bio: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    isVerified: Boolean,
    isOnline: Boolean,
    reviewCount: Int,
    followerCount: String,
    followingCount: Int,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.size(74.dp)) {
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .clip(CircleShape)
                        .background(avatarBg)
                        .border(dims.stroke.st2, colors.primaryContainerBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    PMText(
                        text = initials,
                        style = PMTextStyle.Headline,
                        fontWeight = FontWeight.ExtraBold,
                        color = avatarFg
                    )
                }
                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .size(dims.spacing.s16)
                            .clip(CircleShape)
                            .background(colors.success)
                            .border(dims.stroke.st2, colors.surface, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = dims.spacing.s4),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    PMText(
                        text = displayName,
                        style = PMTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    if (isVerified) {
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(dims.spacing.s16)
                        )
                    }
                }
                PMText(
                    text = username,
                    style = PMTextStyle.Note,
                    color = colors.textLabel
                )
                PMText(
                    text = bio,
                    style = PMTextStyle.Note,
                    color = colors.textSecondary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dims.spacing.s12)
                .height(IntrinsicSize.Min)
                .clip(MaterialTheme.shapes.medium)
                .border(dims.stroke.st1, colors.outlineVariant, MaterialTheme.shapes.medium)
        ) {
            StatCell(value = reviewCount.toString(), label = stringResource(R.string.user_profile_stat_reviews), modifier = Modifier.weight(1f))
            Box(modifier = Modifier.width(dims.stroke.st1).fillMaxHeight().background(colors.outlineVariant))
            StatCell(value = followerCount, label = stringResource(R.string.user_profile_stat_followers), modifier = Modifier.weight(1f))
            Box(modifier = Modifier.width(dims.stroke.st1).fillMaxHeight().background(colors.outlineVariant))
            StatCell(value = followingCount.toString(), label = stringResource(R.string.user_profile_stat_following), modifier = Modifier.weight(1f))
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
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
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
            initials = "AY",
            avatarBg = Color(0xFF164E63),
            avatarFg = Color(0xFF67E8F9),
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
