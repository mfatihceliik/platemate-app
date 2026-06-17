package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateDetailRoute(
    viewModel: PlateDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReview: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateDetailUiEffect.NavigateBack -> onNavigateBack()
                is PlateDetailUiEffect.NavigateToReview -> onNavigateToReview(effect.plateCode)
            }
        }
    }

    PlateDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
