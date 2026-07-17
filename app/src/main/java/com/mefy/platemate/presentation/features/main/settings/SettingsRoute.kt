package com.mefy.platemate.presentation.features.main.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel,
    onBackClick: (() -> Unit)? = null,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToThemeColor: () -> Unit,
    onNavigateToCardStyle: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToNotificationPreferences: () -> Unit,
    onNavigateToAdmin: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                SettingsUiEffect.NavigateToChangePassword -> onNavigateToChangePassword()
                SettingsUiEffect.NavigateToEditProfile -> onNavigateToEditProfile()
                SettingsUiEffect.NavigateToPremium -> onNavigateToPremium()
                SettingsUiEffect.NavigateToThemeColor -> onNavigateToThemeColor()
                SettingsUiEffect.NavigateToCardStyle -> onNavigateToCardStyle()
                SettingsUiEffect.NavigateToLanguage -> onNavigateToLanguage()
                SettingsUiEffect.NavigateToNotificationPreferences -> onNavigateToNotificationPreferences()
                SettingsUiEffect.NavigateToAdmin -> onNavigateToAdmin()
            }
        }
    }

    val onAction = viewModel::onAction
    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    // Stable, hoisted callbacks: rows/buttons skip recomposition while data is unchanged.
    val onRetry = remember(onAction) { { onAction(SettingsUiAction.RetryClicked) } }

    PMBaseScreen(
        status = status,
        onRetry = onRetry,
        loading = { innerPadding ->
            PMCircularProgressIndicator(
                fillMaxSize = true,
                modifier = Modifier.padding(innerPadding)
            )
        },
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_settings_title),
            onBackClick = onBackClick,
            alignment = PMTopBarAlignment.Start
        ),
    ) { innerPadding ->
        SettingsScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
