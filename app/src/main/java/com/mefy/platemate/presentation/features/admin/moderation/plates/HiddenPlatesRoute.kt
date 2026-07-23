package com.mefy.platemate.presentation.features.admin.moderation.plates

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
fun HiddenPlatesRoute(
    viewModel: HiddenPlatesViewModel,
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                HiddenPlatesUiEffect.NavigateBack -> navController.navigateUp()
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
            title = stringResource(R.string.admin_hidden_plates_title)
        ),
        status = status,
        keepTopBarWhileLoading = true,
        onRetry = { viewModel.onAction(HiddenPlatesUiAction.RetryClicked) },
        loading = { p -> PMCircularProgressIndicator(fillMaxSize = true, modifier = Modifier.padding(p)) }
    ) { contentPadding ->
        HiddenPlatesScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}
