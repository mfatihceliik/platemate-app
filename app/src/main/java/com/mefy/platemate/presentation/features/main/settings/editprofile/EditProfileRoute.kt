package com.mefy.platemate.presentation.features.main.settings.editprofile

import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileRoute(
    viewModel: EditProfileViewModel,
    onNavigateBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiMessages(viewModel.uiMessages)

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                EditProfileUiEffect.NavigateBack -> onNavigateBack()
                is EditProfileUiEffect.ShowSnackbar -> onShowSnackbar(effect.message)
            }
        }
    }

    EditProfileScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
