package com.mefy.platemate.presentation.features.admin.moderation.removal

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateRemovalRoute(
    viewModel: PlateRemovalViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateRemovalUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_plate_removal_title),
            onBackClick = { viewModel.onAction(PlateRemovalUiAction.BackClicked) }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = { viewModel.onAction(PlateRemovalUiAction.RetryClicked) },
        contentPadding = PaddingValues(MaterialTheme.pmDimensions.spacing.s16),
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        PlateRemovalScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
