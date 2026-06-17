package com.mefy.platemate.presentation.features.main.profile.settings.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel,
    onBackClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                ChangePasswordUiEffect.NavigateBack -> onBackClick()
                ChangePasswordUiEffect.PasswordChanged -> {
                    onShowSnackbar("Şifre güncellendi")
                    onBackClick()
                }
            }
        }
    }

    ChangePasswordScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}
