package com.mefy.platemate.presentation.features.admin.plateremovalreasons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.components.PMIconButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateRemovalReasonsRoute(
    viewModel: PlateRemovalReasonsViewModel,
    onNavigateToForm: (reasonId: Long?) -> Unit,
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.refresh() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateRemovalReasonsUiEffect.NavigateBack -> navController.navigateUp()
                is PlateRemovalReasonsUiEffect.NavigateToForm -> onNavigateToForm(effect.reasonId)
            }
        }
    }

    val onAction = viewModel::onAction

    val onBackClicked = remember(onAction) { { onAction(PlateRemovalReasonsUiAction.BackClicked) } }
    val onAddClicked = remember(onAction) { { onAction(PlateRemovalReasonsUiAction.AddClicked) } }
    val onRetryClicked = remember(onAction) { { onAction(PlateRemovalReasonsUiAction.RetryClicked) } }

    val status = when {
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.admin_plate_removal_reasons_title),
            actions = {
                PMIconButton(
                    onClick = onAddClicked,
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.admin_plate_removal_reason_add)
                )
            }
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = onRetryClicked,
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        PlateRemovalReasonsScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}

