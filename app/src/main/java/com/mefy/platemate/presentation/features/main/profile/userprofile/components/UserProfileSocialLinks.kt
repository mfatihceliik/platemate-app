package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun UserProfileSocialLinks(
    modifier: Modifier = Modifier,
    links: List<ProfileSocialLinkUiModel>,
    onLinkClick: (ProfileSocialLinkUiModel) -> Unit,
) {
    if (links.isEmpty()) return
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing


    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = spacing.s16, vertical = spacing.s16),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                spacing.s8, Alignment.CenterHorizontally
            ), verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = links, key = { it.id ?: it.platform }) { link ->
                PMIconButton(
                    onClick = { onLinkClick(link) },
                    variant = PMIconButtonVariant.Tonal,
                    size = sizing.iconLg,
                    iconColor = link.iconTint,
                    containerColor = link.backgroundColor,
                    painter = rememberAsyncImagePainter(
                        model = link.iconUrl,
                        error = painterResource(R.drawable.ic_link),
                        placeholder = painterResource(R.drawable.ic_link)
                    ),
                    contentDescription = link.platform,
                )
            }
        }
    }
}

@Preview(name = "UserProfileSocialLinks Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileSocialLinksLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserProfileSocialLinks(
            links = listOf(
                ProfileSocialLinkUiModel(
                    id = 1,
                    platform = "INSTAGRAM",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFFDF2F8),
                    iconTint = Color(0xFFDB2777)
                ), ProfileSocialLinkUiModel(
                    id = 2,
                    platform = "X",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFF1F5F9),
                    iconTint = Color(0xFF0F172A)
                ), ProfileSocialLinkUiModel(
                    id = 3,
                    platform = "GITHUB",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFF1F5F9),
                    iconTint = Color(0xFF0F172A)
                )
            ), onLinkClick = {})
    }
}

@Preview(name = "UserProfileSocialLinks Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileSocialLinksDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserProfileSocialLinks(
            links = listOf(
                ProfileSocialLinkUiModel(
                    id = 1,
                    platform = "INSTAGRAM",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFFDF2F8),
                    iconTint = Color(0xFFDB2777)
                ), ProfileSocialLinkUiModel(
                    id = 2,
                    platform = "X",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFF1F5F9),
                    iconTint = Color(0xFF0F172A)
                ), ProfileSocialLinkUiModel(
                    id = 3,
                    platform = "GITHUB",
                    url = "",
                    iconUrl = null,
                    backgroundColor = Color(0xFFF1F5F9),
                    iconTint = Color(0xFF0F172A)
                )
            ), onLinkClick = {})
    }
}
