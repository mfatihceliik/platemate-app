package com.mefy.platemate.presentation.features.main.platedetail.removal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateRemovalRequestRoute(
    viewModel: PlateRemovalRequestViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateRemovalRequestUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    PlateRemovalRequestScreen(state = state, onAction = viewModel::onAction, modifier = modifier)
}
