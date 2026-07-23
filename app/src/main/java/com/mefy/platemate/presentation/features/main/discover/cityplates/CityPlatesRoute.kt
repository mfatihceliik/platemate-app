package com.mefy.platemate.presentation.features.main.discover.cityplates

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.app.providers.LocalNavController
import com.mefy.platemate.presentation.common.basescreen.PMBaseScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CityPlatesRoute(
    modifier: Modifier = Modifier,
    viewModel: CityPlatesViewModel,
    onNavigateToPlateDetail: (String) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.uiEffects.collectLatest { effect ->
            when (effect) {
                CityPlatesUiEffect.NavigateBack -> navController.navigateUp()
                is CityPlatesUiEffect.NavigateToPlateDetail -> onNavigateToPlateDetail(effect.plateCode)
            }
        }
    }

    val onAction = viewModel::onAction

    val onBack = remember(onAction) { { onAction(CityPlatesUiAction.BackClicked) } }

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        state.isEmpty -> ScreenStatus.Empty
        else -> ScreenStatus.Content
    }


    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = state.cityName
        ),
        viewModel = viewModel,
        status = status
    ) { innerPadding ->
        CityPlatesScreen(
            state = state,
            onAction = onAction,
            lazyListState = lazyListState,
            innerPadding = innerPadding
        )
    }
}
