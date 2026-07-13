package com.mefy.platemate.presentation.features.main.settings.cardstyle

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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardStyleRoute(
    viewModel: CardStyleViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPremiumInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                CardStyleUiEffect.NavigateBack -> onNavigateBack()
                CardStyleUiEffect.NavigateToPremiumInfo -> onNavigateToPremiumInfo()
            }
        }
    }

    val onAction = viewModel::onAction
    val onBack = remember(onAction) { { onAction(CardStyleUiAction.BackClicked) } }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.card_style_title),
            onBackClick = onBack
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
