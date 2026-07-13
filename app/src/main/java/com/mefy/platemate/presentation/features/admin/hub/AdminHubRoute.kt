package com.mefy.platemate.presentation.features.admin.hub

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

/**
 * Hub menüsü backend'den gelir (localized başlık + badge). Route yalnız state/effect
 * bağlar; code → destination eşlemesi nav katmanında ([onItemClick] callback'i).
 */
@Composable
fun AdminHubRoute(
    viewModel: AdminHubViewModel,
    onBackClick: () -> Unit,
    onItemClick: (code: String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    // Detay ekranından dönüşte badge sayıları tazelensin.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                AdminHubUiEffect.NavigateBack -> onBackClick()
                is AdminHubUiEffect.NavigateToItem -> onItemClick(effect.code)
            }
        }
    }

    val onAction = viewModel::onAction

    val onBackClicked = remember(onAction) { { onAction(AdminHubUiAction.BackClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(AdminHubUiAction.RetryClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_panel_title),
            onBackClick = onBackClicked
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = onRetryClicked,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        AdminHubScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
