package com.mefy.platemate.presentation.features.main.profile.settings.themecolor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ThemeColorRoute(
    viewModel: ThemeColorViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ThemeColorUiEffect.NavigateBack -> onBackClick()
            }
        }
    }

    ThemeColorScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
