package com.mefy.platemate.presentation.features.main.settings.cardstyle

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
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardStyleRoute(
    modifier: Modifier = Modifier,
    viewModel: CardStyleViewModel,
    onNavigateToPremiumInfo: () -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                CardStyleUiEffect.NavigateBack -> navController.navigateUp()
                CardStyleUiEffect.NavigateToPremiumInfo -> onNavigateToPremiumInfo()
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(CardStyleUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.card_style_title)
        ),
        status = ScreenStatus.Content
    ) { innerPadding ->
        CardStyleScreen(
            state = state,
            onAction = onAction,
            innerPadding = innerPadding
        )
    }
}
