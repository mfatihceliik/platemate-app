package com.mefy.platemate.presentation.features.main.profile.userprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileSocialLinks(
    modifier: Modifier = Modifier,
    links: List<ProfileSocialLinkUiModel>,
    onLinkClick: (ProfileSocialLinkUiModel) -> Unit,
) {
    if (links.isEmpty()) return
    val dims = MaterialTheme.pmDimensions
    MaterialTheme.pmColors
    MaterialTheme.shapes.small

    PMCard(
        modifier = Modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                dims.spacing.s8,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = links, key = { it.id ?: it.platform }) { link ->
                PMIconButton(
                    onClick = { onLinkClick(link) },

                    ) {
                    PMIcon(
                        painter = rememberAsyncImagePainter(
                            model = link.iconUrl,
                            error = painterResource(R.drawable.ic_link),
                            placeholder = painterResource(R.drawable.ic_link)
                        ),
                        size = dims.sizing.iconHuge,
                        contentDescription = link.platform,
                        tint = link.iconTint,
                        containerColor = link.backgroundColor
                    )
                }
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
                ProfileSocialLinkUiModel(
                    id = 1, platform = "INSTAGRAM", url = "", iconUrl = null, backgroundColor = Color(0xFFFDF2F8), iconTint = Color(0xFFDB2777)
                ),
                ProfileSocialLinkUiModel(
                    id = 2, platform = "X", url = "", iconUrl = null, backgroundColor = Color(0xFFF1F5F9), iconTint = Color(0xFF0F172A)
                ),
                ProfileSocialLinkUiModel(
                    id = 3, platform = "GITHUB", url = "", iconUrl = null, backgroundColor = Color(0xFFF1F5F9), iconTint = Color(0xFF0F172A)
                )
            ),
            onLinkClick = {}
        )
    }
}

@Preview(name = "UserProfileSocialLinks Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileSocialLinksDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        MaterialTheme.pmColors
        UserProfileSocialLinks(
            links = listOf(
                ProfileSocialLinkUiModel(
                    id = 1, platform = "INSTAGRAM", url = "", iconUrl = null, backgroundColor = Color(0xFFFDF2F8), iconTint = Color(0xFFDB2777)
                ),
                ProfileSocialLinkUiModel(
                    id = 2, platform = "X", url = "", iconUrl = null, backgroundColor = Color(0xFFF1F5F9), iconTint = Color(0xFF0F172A)
                ),
                ProfileSocialLinkUiModel(
                    id = 3, platform = "GITHUB", url = "", iconUrl = null, backgroundColor = Color(0xFFF1F5F9), iconTint = Color(0xFF0F172A)
                )
            ),
            onLinkClick = {}
        )
    }
}
