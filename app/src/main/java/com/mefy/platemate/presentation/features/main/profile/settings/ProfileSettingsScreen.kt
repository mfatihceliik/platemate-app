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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTopBarConfig
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.features.main.profile.settings.components.BellIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.CrownIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.DropletIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.GlobeIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.LinkIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.LockIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.PersonIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.LogoutIcon
import com.mefy.platemate.presentation.features.main.profile.settings.components.SettingsDivider
import com.mefy.platemate.presentation.features.main.profile.settings.components.SettingsNavRow
import com.mefy.platemate.presentation.features.main.profile.settings.components.SettingsSection
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
        topBarConfig = PMTopBarConfig.Simple(
            title = stringResource(R.string.profile_settings_title),
            onBackClick = onBackClick
        )
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            item(key = "account_section") {
                SettingsSection(
                    label = stringResource(R.string.profile_settings_section_account)
                ) {
                    SettingsNavRow(
                        iconBg = colors.primaryContainer,
                        iconContent = { LockIcon() },
                        label = stringResource(R.string.profile_change_password_title),
                        onClick = { onAction(ProfileSettingsUiAction.ChangePasswordClicked) }
                    )
                    SettingsDivider()
                    SettingsNavRow(
                        iconBg = colors.primaryContainer,
                        iconContent = { PersonIcon() },
                        label = stringResource(R.string.profile_settings_edit_profile),
                        onClick = { onAction(ProfileSettingsUiAction.EditProfileClicked) }
                    )
                    SettingsDivider()
                    SettingsNavRow(
                        iconBg = Color(0xFFFEF3C7),
                        iconContent = { CrownIcon() },
                        label = if (state.premiumActive) {
                            stringResource(R.string.profile_setting_premium_info)
                        } else {
                            stringResource(R.string.profile_settings_premium_go)
                        },
                        badge = if (!state.premiumActive) "PRO" else null,
                        onClick = { onAction(ProfileSettingsUiAction.PremiumClicked) }
                    )
                }
            }

            item(key = "app_section") {
                SettingsSection(
                    label = stringResource(R.string.profile_settings_section_app)
                ) {
                    SettingsNavRow(
                        iconBg = colors.primaryContainer,
                        iconContent = { DropletIcon() },
                        label = stringResource(R.string.profile_settings_theme_color),
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(dims.sizing.iconMd)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        },
                        onClick = { onAction(ProfileSettingsUiAction.ThemeColorClicked) }
                    )
                    SettingsDivider()
                    SettingsNavRow(
                        iconBg = colors.categoryGreenBg,
                        iconContent = { GlobeIcon() },
                        label = stringResource(R.string.profile_setting_language),
                        trailing = {
                            PMText(
                                text = state.language.displayName(),
                                style = PMTextStyle.Note,
                                color = colors.textLabel
                            )
                        },
                        onClick = { onAction(ProfileSettingsUiAction.LanguageClicked) }
                    )
                    SettingsDivider()
                    SettingsNavRow(
                        iconBg = Color(0xFFFFF1F2),
                        iconContent = { BellIcon() },
                        label = stringResource(R.string.profile_setting_notification_preferences),
                        onClick = { onAction(ProfileSettingsUiAction.NotificationPreferencesClicked) }
                    )
                }
            }

            item(key = "profile_section") {
                SettingsSection(
                    label = stringResource(R.string.profile_settings_section_profile)
                ) {
                    SettingsNavRow(
                        iconBg = Color(0xFFF3E8FF),
                        iconContent = { LinkIcon() },
                        label = stringResource(R.string.profile_settings_social_accounts),
                        trailing = {
                            if (state.socialLinksCount > 0) {
                                PMText(
                                    text = stringResource(
                                        R.string.profile_settings_social_count,
                                        state.socialLinksCount
                                    ),
                                    style = PMTextStyle.Note,
                                    color = colors.textLabel
                                )
                            }
                        },
                        onClick = { onAction(ProfileSettingsUiAction.SocialLinksClicked) }
                    )
                }
            }

            item(key = "sign_out") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dims.sizing.ctaHeightLarge)
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                        .border(dims.stroke.st1, MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.medium)
                        .debouncedClickable { onAction(ProfileSettingsUiAction.SignOutClicked) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LogoutIcon()
                    PMText(
                        text = stringResource(R.string.profile_setting_sign_out),
                        style = PMTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error,
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

private fun com.mefy.platemate.domain.model.language.AppLanguage.displayName(): String {
    return when (this) {
        com.mefy.platemate.domain.model.language.AppLanguage.TR -> "Türkçe"
        com.mefy.platemate.domain.model.language.AppLanguage.EN -> "English"
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
