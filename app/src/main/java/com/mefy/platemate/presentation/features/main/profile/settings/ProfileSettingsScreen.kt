package com.mefy.platemate.presentation.features.main.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMRowDivider
import com.mefy.platemate.presentation.components.PMRowGroup
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.settings.components.ChevronIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.LogoutIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.ProBadge
import com.mefy.platemate.presentation.features.main.profile.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileSettingsScreen(
    state: ProfileSettingsUiState,
    onAction: (ProfileSettingsUiAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_settings_title),
            onBackClick = onBackClick
        )
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            // ── Account ──────────────────────────────────────
            item(key = "account_label") {
                SectionLabel(text = stringResource(R.string.profile_settings_section_account))
            }
            item(key = "account_group") {
                PMRowGroup {
                    PMRowItem(
                        title = stringResource(R.string.profile_change_password_title),
                        leadingIcon = Icons.Filled.Lock,
                        leadingIconTint = colors.onPrimaryContainer,
                        leadingContainerColor = colors.primaryContainer,
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.ChangePasswordClicked) }
                    )
                    PMRowDivider()
                    PMRowItem(
                        title = stringResource(R.string.profile_settings_edit_profile),
                        leadingIcon = Icons.Filled.Person,
                        leadingIconTint = colors.onPrimaryContainer,
                        leadingContainerColor = colors.primaryContainer,
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.EditProfileClicked) }
                    )
                    PMRowDivider()
                    PMRowItem(
                        title = if (state.premiumActive) {
                            stringResource(R.string.profile_setting_premium_info)
                        } else {
                            stringResource(R.string.profile_settings_premium_go)
                        },
                        leadingIcon = Icons.Filled.Star,
                        leadingIconTint = colors.star,
                        leadingContainerColor = colors.warning.copy(alpha = 0.4f),
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.PremiumClicked) },
                        trailing = if (!state.premiumActive) {
                            {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                                ) {
                                    ProBadge()
                                    ChevronIcon()
                                }
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            // ── Application ──────────────────────────────────
            item(key = "app_label") {
                SectionLabel(text = stringResource(R.string.profile_settings_section_app))
            }
            item(key = "app_group") {
                PMRowGroup {
                    PMRowItem(
                        title = stringResource(R.string.profile_settings_theme_color),
                        leadingIcon = Icons.Filled.Palette,
                        leadingIconTint = colors.primary,
                        leadingContainerColor = colors.primaryContainer,
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.ThemeColorClicked) },
                        trailing = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(dims.sizing.iconMd)
                                        .clip(CircleShape)
                                        .background(colors.primary)
                                )
                                ChevronIcon()
                            }
                        }
                    )
                    PMRowDivider()
                    PMRowItem(
                        title = stringResource(R.string.profile_setting_language),
                        leadingIcon = Icons.Filled.Language,
                        leadingIconTint = colors.categoryGreenIcon,
                        leadingContainerColor = colors.categoryGreenBg,
                        trailingText = state.languageLabel.resolve(),
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.LanguageClicked) }
                    )
                    PMRowDivider()
                    PMRowItem(
                        title = stringResource(R.string.profile_setting_notification_preferences),
                        leadingIcon = Icons.Filled.Notifications,
                        leadingIconTint = colors.error,
                        leadingContainerColor = colors.errorContainer,
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.NotificationPreferencesClicked) }
                    )
                }
            }

            // ── Profile ──────────────────────────────────────
            item(key = "profile_label") {
                SectionLabel(text = stringResource(R.string.profile_settings_section_profile))
            }
            item(key = "profile_group") {
                PMRowGroup {
                    PMRowItem(
                        title = stringResource(R.string.profile_settings_social_accounts),
                        leadingIcon = Icons.Filled.Link,
                        leadingIconTint = colors.tertiary,
                        leadingContainerColor = colors.tertiaryContainer,
                        trailingText = if (state.socialLinksCount > 0) {
                            stringResource(R.string.profile_settings_social_count, state.socialLinksCount)
                        } else {
                            null
                        },
                        showCard = false,
                        onClick = { onAction(ProfileSettingsUiAction.SocialLinksClicked) }
                    )
                }
            }

            // ── Sign out ─────────────────────────────────────
            item(key = "sign_out") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dims.spacing.s8)
                        .height(dims.sizing.ctaHeightLarge)
                        .background(colors.surface, MaterialTheme.shapes.medium)
                        .border(dims.stroke.st1, colors.errorContainer, MaterialTheme.shapes.medium)
                        .debouncedClickable { onAction(ProfileSettingsUiAction.SignOutClicked) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LogoutIcon()
                    PMText(
                        text = stringResource(R.string.profile_setting_sign_out),
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.error,
                        modifier = Modifier.padding(start = dims.spacing.s8)
                    )
                }
            }

            item(key = "version") {
                PMText(
                    text = stringResource(R.string.profile_settings_version),
                    style = PMTextStyle.Note,
                    color = colors.textLabel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dims.spacing.s4),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(name = "Settings Light", showBackground = true)
@Composable
private fun SettingsLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileSettingsScreen(
            state = ProfileSettingsUiState(isLoading = false, socialLinksCount = 2),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Settings Dark", showBackground = true)
@Composable
private fun SettingsDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileSettingsScreen(
            state = ProfileSettingsUiState(isLoading = false),
            onAction = {},
            onBackClick = {}
        )
    }
}