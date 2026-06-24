package com.mefy.platemate.presentation.features.main.settings.language

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LanguageRoute(
    viewModel: LanguageViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                LanguageUiEffect.NavigateBack -> onBackClick()
            }
        }
    }

    LanguageScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
