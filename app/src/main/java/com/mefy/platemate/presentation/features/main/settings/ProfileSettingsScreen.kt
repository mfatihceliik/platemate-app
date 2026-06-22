package com.mefy.platemate.presentation.features.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.BuildConfig
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMLoading
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMRowDivider
import com.mefy.platemate.presentation.components.PMRowGroup
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.settings.components.ChevronIcon
import com.mefy.platemate.presentation.features.main.settings.components.ProBadge
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileSettingsScreen(
    state: ProfileSettingsUiState,
    onAction: (ProfileSettingsUiAction) -> Unit,
    onBackClick: (() -> Unit)? = null,
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
        status = status,
        onRetry = { onAction(ProfileSettingsUiAction.RetryClicked) },
        loading = { innerPadding -> PMLoading(modifier = Modifier.padding(innerPadding)) },
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_settings_title), onBackClick = onBackClick
        )) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = dims.spacing.s16,
                    vertical = dims.spacing.s8
                ),
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
                            leadingIconTint = colors.primary,
                            leadingContainerColor = colors.primaryContainer,
                            showCard = false,
                            onClick = { onAction(ProfileSettingsUiAction.ChangePasswordClicked) })
                        PMRowDivider()
                        PMRowItem(
                            title = stringResource(R.string.profile_settings_edit_profile),
                            leadingIcon = Icons.Filled.Person,
                            leadingIconTint = colors.primary,
                            leadingContainerColor = colors.primaryContainer,
                            showCard = false,
                            onClick = { onAction(ProfileSettingsUiAction.EditProfileClicked) })
                        PMRowDivider()
                        PMRowItem(
                            title = if (state.premiumActive) {
                                stringResource(R.string.profile_setting_premium_info)
                            } else {
                                stringResource(R.string.profile_settings_premium_go)
                            },
                            leadingIcon = Icons.Filled.Star,
                            leadingIconTint = colors.primary,
                            leadingContainerColor = colors.primaryContainer,
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
                            })
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
                            })
                        PMRowDivider()
                        PMRowItem(
                            title = stringResource(R.string.profile_setting_language),
                            leadingIcon = Icons.Filled.Language,
                            leadingIconTint = colors.primary,
                            leadingContainerColor = colors.primaryContainer,
                            trailingText = state.languageLabel.resolve(),
                            showCard = false,
                            onClick = { onAction(ProfileSettingsUiAction.LanguageClicked) })
                        PMRowDivider()
                        PMRowItem(
                            title = stringResource(R.string.profile_setting_notification_preferences),
                            leadingIcon = Icons.Filled.Notifications,
                            leadingIconTint = colors.primary,
                            leadingContainerColor = colors.primaryContainer,
                            showCard = false,
                            onClick = { onAction(ProfileSettingsUiAction.NotificationPreferencesClicked) })
                    }
                }
            }

            PMButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dims.spacing.s12),
                text = stringResource(R.string.profile_setting_sign_out),
                onClick = { onAction(ProfileSettingsUiAction.SignOutClicked) },
                colors = ButtonColors(
                    containerColor = colors.errorContainer,
                    contentColor = colors.error,
                    disabledContentColor = colors.disabled,
                    disabledContainerColor = colors.disabled
                ),
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        tint = colors.error
                    )
                })

            PMText(
                text = stringResource(R.string.profile_settings_version, BuildConfig.VERSION_NAME),
                style = PMTextStyle.Note,
                color = colors.textLabel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dims.spacing.s12),
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
                isLoading = false,
                socialLinksCount = 2
            ),
            onAction = {},
            onBackClick = {})
    }
}

@Preview(name = "Settings Dark", showBackground = true)
@Composable
private fun SettingsDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileSettingsScreen(
            state = ProfileSettingsUiState(isLoading = false),
            onAction = {},
            onBackClick = {})
    }
}