package com.mefy.platemate.presentation.features.main.settings.sociallinks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatform
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun SocialLinksScreen(
    state: SocialLinksUiState,
    onAction: (SocialLinksUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_setting_social_links),
            onBackClick = { onAction(SocialLinksUiAction.BackClicked) }
        ),
        status = status,
        onRetry = { onAction(SocialLinksUiAction.RetryClicked) },
        loading = { innerPadding ->
            PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding))
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            item(key = "description") {
                PMText(
                    text = stringResource(R.string.profile_social_description),
                    style = PMTextStyle.Caption,
                    color = colors.textTertiary
                )
            }

            item(key = "connected_section") {
                SectionLabel(text = stringResource(R.string.profile_social_connected_section))
            }

            if (state.links.isEmpty()) {
                item(key = "empty") {
                    PMText(
                        text = stringResource(R.string.profile_social_empty),
                        style = PMTextStyle.Caption,
                        color = colors.textLabel
                    )
                }
            } else {
                items(
                    items = state.links,
                    key = { link -> link.id ?: link.platform }
                ) { link ->
                    SocialLinkRow(
                        link = link,
                        onDeleteClick = { link.id?.let { onAction(SocialLinksUiAction.DeleteClicked(it)) } }
                    )
                }
            }

            item(key = "add_section") {
                SectionLabel(text = stringResource(R.string.profile_social_add_section))
            }

            item(key = "add_form") {
                AddLinkForm(state = state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun SocialLinkRow(
    link: ProfileSocialLinkUiModel,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = dims.spacing.s12, vertical = dims.spacing.s8)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMIcon(
                painter = rememberAsyncImagePainter(
                    model = link.iconUrl,
                    error = painterResource(R.drawable.ic_link),
                    placeholder = painterResource(R.drawable.ic_link)
                ),
                contentDescription = link.platform,
                tint = link.iconTint,
                containerColor = link.backgroundColor
            )
            PMText(
                text = link.url,
                style = PMTextStyle.Caption,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            PMIconButton(
                onClick = onDeleteClick,
                contentDescription = stringResource(R.string.profile_social_delete_button)
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = colors.error,
                    size = dims.sizing.iconSm
                )
            }
        }
    }
}

@Composable
private fun AddLinkForm(
    state: SocialLinksUiState,
    onAction: (SocialLinksUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(dims.spacing.s16)
    ) {
        PMText(
            text = stringResource(R.string.profile_social_add_title),
            style = PMTextStyle.Body,
            color = colors.textPrimary
        )

        // Platform seçimi: seçili olan renkli kenarlıkla vurgulanır.
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dims.spacing.s12),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            items(items = state.availablePlatforms, key = { it.id }) { platform ->
                val isSelected = platform.code == state.selectedPlatformId
                Box(
                    modifier = Modifier
                        .size(dims.sizing.avatarIconInner)
                        .clip(RoundedCornerShape(dims.radius.r12))
                        .background(if (isSelected) colors.primaryContainer else platform.backgroundColor)
                        .debouncedClickable { onAction(SocialLinksUiAction.PlatformSelected(platform.code)) },
                    contentAlignment = Alignment.Center
                ) {
                    PMIcon(
                        painter = rememberAsyncImagePainter(
                            model = platform.iconUrl,
                            error = painterResource(R.drawable.ic_link),
                            placeholder = painterResource(R.drawable.ic_link)
                        ),
                        contentDescription = platform.displayName,
                        tint = if (isSelected) colors.primary else platform.iconTint,
                        size = dims.sizing.iconLg
                    )
                }
            }
        }

        PMTextField(
            value = state.urlInput,
            onValueChange = { onAction(SocialLinksUiAction.UrlChanged(it)) },
            label = stringResource(R.string.profile_social_url_label),
            modifier = Modifier.fillMaxWidth()
        )

        PMButton(
            text = stringResource(R.string.profile_social_add_button),
            onClick = { onAction(SocialLinksUiAction.AddClicked) },
            enabled = state.isAddEnabled,
            loading = state.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dims.spacing.s12)
        )
    }
}

private fun previewState() = SocialLinksUiState(
    isLoading = false,
    availablePlatforms = listOf(
        SocialPlatform(1, "INSTAGRAM", "Instagram", null, Color(0xFFFDF2F8), Color(0xFFDB2777)),
        SocialPlatform(2, "X", "X", null, Color(0xFFF1F5F9), Color(0xFF0F172A))
    ),
    links = listOf(
        ProfileSocialLinkUiModel(
            id = 1,
            platform = "INSTAGRAM",
            url = "https://instagram.com/platemate",
            iconUrl = null,
            backgroundColor = Color(0xFFFDF2F8),
            iconTint = Color(0xFFDB2777)
        ),
        ProfileSocialLinkUiModel(
            id = 2,
            platform = "X",
            url = "https://x.com/platemate",
            iconUrl = null,
            backgroundColor = Color(0xFFF1F5F9),
            iconTint = Color(0xFF0F172A)
        )
    ),
    selectedPlatformId = "INSTAGRAM",
    urlInput = "https://instagram.com/yeni"
)

@Preview(name = "SocialLinks Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SocialLinksScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SocialLinksScreen(state = previewState(), onAction = {})
    }
}

@Preview(name = "SocialLinks Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SocialLinksScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SocialLinksScreen(state = previewState(), onAction = {})
    }
}
