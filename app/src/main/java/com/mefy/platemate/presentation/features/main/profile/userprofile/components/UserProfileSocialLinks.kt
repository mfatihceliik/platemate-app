package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.settings.sociallinks.components.getPlatformInfo
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileSocialLinkUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileSocialLinks(
    modifier: Modifier = Modifier,
    links: List<UserProfileSocialLinkUiModel>,
    onLinkClick: (UserProfileSocialLinkUiModel) -> Unit,
) {
    if (links.isEmpty()) return
    val dims = MaterialTheme.pmDimensions
    val iconShape = MaterialTheme.shapes.small

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        links.forEach { link ->
            val info = getPlatformInfo(link.platform)
            Row(
                modifier = Modifier
                    .size(dims.sizing.avatarIconInner)
                    .clip(iconShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(dims.stroke.st1, MaterialTheme.colorScheme.outline, iconShape)
                    .debouncedClickable { onLinkClick(link) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(info.iconRes),
                    contentDescription = info.displayName,
                    tint = info.iconTint,
                    modifier = Modifier.size(dims.sizing.iconMd)
                )
            }
        }
    }
}

@Preview(name = "UserProfileSocialLinks Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileSocialLinksLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        UserProfileSocialLinks(
            links = listOf(
                UserProfileSocialLinkUiModel(platform = "INSTAGRAM", url = ""),
                UserProfileSocialLinkUiModel(platform = "X", url = ""),
                UserProfileSocialLinkUiModel(platform = "WEBSITE", url = "")
            ),
            onLinkClick = {},
            modifier = Modifier.padding(dims.spacing.s16)
        )
    }
}

@Preview(name = "UserProfileSocialLinks Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileSocialLinksDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        UserProfileSocialLinks(
            links = listOf(
                UserProfileSocialLinkUiModel(platform = "INSTAGRAM", url = ""),
                UserProfileSocialLinkUiModel(platform = "X", url = ""),
                UserProfileSocialLinkUiModel(platform = "WEBSITE", url = "")
            ),
            onLinkClick = {},
            modifier = Modifier.padding(dims.spacing.s16)
        )
    }
}
