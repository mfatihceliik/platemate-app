package com.mefy.platemate.presentation.features.main.settings.language

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
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

    val onAction = viewModel::onAction

    val onBack = remember(onAction) { { onAction(LanguageUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_language_title),
            onBackClick = onBack
        ),
    ) { innerPadding ->
        LanguageScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = modifier,
            innerPadding = innerPadding
        )
    }
}
