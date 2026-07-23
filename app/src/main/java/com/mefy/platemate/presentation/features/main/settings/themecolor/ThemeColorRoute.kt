package com.mefy.platemate.presentation.features.main.settings.themecolor

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
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.ThemeColorShimmerContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ThemeColorRoute(
    modifier: Modifier = Modifier,
    viewModel: ThemeColorViewModel
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ThemeColorUiEffect.NavigateBack -> navController.navigateUp()
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(ThemeColorUiAction.BackClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier, topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_theme_color_title)
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = viewModel::retry,
        loading = { innerPadding ->
            ThemeColorShimmerContent(
                modifier = Modifier.padding(innerPadding),
                gridSize = state.gridSize
            )
        }
    ) { innerPadding ->
        ThemeColorScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
