package com.mefy.platemate.presentation.features.main.platedetail.actions

import androidx.compose.foundation.layout.padding
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
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlateActionsRoute(
    modifier: Modifier = Modifier,
    viewModel: PlateActionsViewModel,
    onNavigateToRemoval: (plateId: Long, plateCode: String) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                PlateActionsUiEffect.NavigateBack -> navController.navigateUp()
                is PlateActionsUiEffect.NavigateToRemoval -> onNavigateToRemoval(effect.plateId, effect.plateCode)
            }
        }
    }

    val onAction = viewModel::onAction
    val onBackClicked = remember(onAction) { { onAction(PlateActionsUiAction.BackClicked) } }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.platedetail_actions_title)
        ),
        status = status,
        loading = { innerPadding ->
            PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(innerPadding))
        }
    ) { innerPadding ->
        PlateActionsScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
