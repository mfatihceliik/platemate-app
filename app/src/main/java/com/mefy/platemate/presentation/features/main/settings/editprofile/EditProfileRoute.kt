package com.mefy.platemate.presentation.features.main.settings.editprofile

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
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileRoute(
    viewModel: EditProfileViewModel,
    onNavigateBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                EditProfileUiEffect.NavigateBack -> onNavigateBack()
                is EditProfileUiEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        }
    }

    val onAction = viewModel::onAction

    val status = when {
        state.isLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage!!)
        else -> ScreenStatus.Content
    }

    // Stable, hoisted callbacks: fields/buttons skip recomposition while their data is unchanged.
    val onBack = remember(onAction) { { onAction(EditProfileUiAction.BackClicked) } }
    val onRetry = remember(onAction) { { onAction(EditProfileUiAction.RetryClicked) } }

    PMBaseScreen(
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.edit_profile_title),
            onBackClick = onBack
        ),
        status = status,
        onRetry = onRetry,
        loading = { innerPadding ->
            PMCircularProgressIndicator(
                fillMaxSize = true,
                modifier = Modifier.padding(innerPadding)
            )
        },
    ) { innerPadding ->
        EditProfileScreen(
            state = state,
            onAction = viewModel::onAction,
            innerPadding = innerPadding
        )
    }
}
