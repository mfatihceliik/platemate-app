package com.mefy.platemate.presentation.features.main.profile.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileSettingsRoute(
    viewModel: ProfileSettingsViewModel,
    onBackClick: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToThemeColor: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToNotificationPreferences: () -> Unit,
    onNavigateToSocialLinks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ProfileSettingsUiEffect.NavigateToChangePassword -> onNavigateToChangePassword()
                ProfileSettingsUiEffect.NavigateToEditProfile -> onNavigateToEditProfile()
                ProfileSettingsUiEffect.NavigateToPremium -> onNavigateToPremium()
                ProfileSettingsUiEffect.NavigateToThemeColor -> onNavigateToThemeColor()
                ProfileSettingsUiEffect.NavigateToLanguage -> onNavigateToLanguage()
                ProfileSettingsUiEffect.NavigateToNotificationPreferences -> onNavigateToNotificationPreferences()
                ProfileSettingsUiEffect.NavigateToSocialLinks -> onNavigateToSocialLinks()
            }
        }
    }

    ProfileSettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
