package com.mefy.platemate.presentation.features.main.settings

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.BuildConfig
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMRowPosition
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.settings.components.ChevronIcon
import com.mefy.platemate.presentation.features.main.settings.components.ProBadge
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileSettingsScreen(
    modifier: Modifier = Modifier,
    state: ProfileSettingsUiState,
    onAction: (ProfileSettingsUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Stable, hoisted callbacks: rows/buttons skip recomposition while data is unchanged.
    val onChangePassword =
        remember(onAction) { { onAction(ProfileSettingsUiAction.ChangePasswordClicked) } }
    val onEditProfile =
        remember(onAction) { { onAction(ProfileSettingsUiAction.EditProfileClicked) } }
    val onPremium = remember(onAction) { { onAction(ProfileSettingsUiAction.PremiumClicked) } }
    val onThemeColor =
        remember(onAction) { { onAction(ProfileSettingsUiAction.ThemeColorClicked) } }
    val onCardStyle =
        remember(onAction) { { onAction(ProfileSettingsUiAction.CardStyleClicked) } }
    val onLanguage = remember(onAction) { { onAction(ProfileSettingsUiAction.LanguageClicked) } }
    val onNotificationPrefs =
        remember(onAction) { { onAction(ProfileSettingsUiAction.NotificationPreferencesClicked) } }
    val onAdminPanel =
        remember(onAction) { { onAction(ProfileSettingsUiAction.AdminPanelClicked) } }
    val onSignOut = remember(onAction) { { onAction(ProfileSettingsUiAction.SignOutClicked) } }


    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = dims.spacing.s16,
            vertical = dims.spacing.s8
        ),
        verticalArrangement = spacedByWithFooter()
    ) {
        item {
            PMSectionLabel(text = stringResource(R.string.profile_settings_section_account))
        }
        item {
            PMRowItem(
                title = stringResource(R.string.profile_settings_edit_profile),
                leadingIcon = Icons.Filled.Person,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Top,
                onClick = onEditProfile
            )
        }
        item {
            PMRowItem(
                title = stringResource(R.string.profile_change_password_title),
                leadingIcon = Icons.Filled.Lock,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Middle,
                onClick = onChangePassword
            )
        }
        item {
            PMRowItem(
                title = if (state.premiumActive) {
                    stringResource(R.string.profile_setting_premium_info)
                } else {
                    stringResource(R.string.profile_settings_premium_go)
                },
                leadingIcon = Icons.Filled.Star,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Bottom,
                onClick = onPremium,
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

        item {
            PMSectionLabel(text = stringResource(R.string.profile_settings_section_app))
        }
        item {
            PMRowItem(
                title = stringResource(R.string.profile_settings_theme_color),
                leadingIcon = Icons.Filled.Palette,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Top,
                onClick = onThemeColor,
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
                })
        }
        item {
            PMRowItem(
                title = stringResource(R.string.profile_settings_card_style),
                leadingIcon = Icons.Filled.Style,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Middle,
                onClick = onCardStyle,
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
        item {
            PMRowItem(
                title = stringResource(R.string.profile_setting_language),
                leadingIcon = Icons.Filled.Language,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                trailingText = state.languageLabel.resolve(),
                position = PMRowPosition.Middle,
                onClick = onLanguage
            )
        }
        item {
            PMRowItem(
                title = stringResource(R.string.profile_setting_notification_preferences),
                leadingIcon = Icons.Filled.Notifications,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Bottom,
                onClick = onNotificationPrefs
            )
        }

        if (state.isAdmin) {
            item {
                PMSectionLabel(text = stringResource(R.string.admin_panel_title))
            }
            item {
                PMRowItem(
                    title = stringResource(R.string.admin_moderation_title),
                    leadingIcon = Icons.Filled.AdminPanelSettings,
                    leadingIconTint = colors.primary,
                    leadingContainerColor = colors.primaryContainer,
                    position = PMRowPosition.Single,
                    onClick = onAdminPanel
                )
            }
        }

        item {
            PMButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dims.spacing.s8),
                text = stringResource(R.string.profile_setting_sign_out),
                onClick = onSignOut,
                colors = ButtonColors(
                    containerColor = colors.error.copy(alpha = .40f), //.errorContainer,
                    contentColor = colors.error,
                    disabledContentColor = colors.disabled,
                    disabledContainerColor = colors.disabled
                ),
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        tint = colors.error
                    )
                }
            )

            PMText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.profile_settings_version, BuildConfig.VERSION_NAME),
                style = PMTextStyle.Note,
                color = colors.textLabel,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Settings Light", showBackground = true)
@Composable
private fun SettingsLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileSettingsScreen(
            state = ProfileSettingsUiState(
                isLoading = false
            ),
            onAction = {},
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
        )
    }
}
