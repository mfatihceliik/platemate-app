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
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ThemeColorRoute(
    viewModel: ThemeColorViewModel, onBackClick: () -> Unit, modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ThemeColorUiEffect.NavigateBack -> onBackClick()
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(ThemeColorUiAction.BackClicked) } }
    val status = if (state.isLoading) ScreenStatus.Loading else ScreenStatus.Content

    PMBaseScreen(
        modifier = modifier, topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_theme_color_title), onBackClick = onBack
        ),
        status = status, loading = { innerPadding ->
            PMCircularProgressIndicator(
                fillMaxSize = true, modifier = Modifier.padding(innerPadding)
            )
        }) { innerPadding ->
        ThemeColorScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
