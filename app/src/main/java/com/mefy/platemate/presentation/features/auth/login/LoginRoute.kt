package com.mefy.platemate.presentation.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.common.messaging.HandleUiMessages

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    prefillEmail: String?,
    onNavigateAfterLogin: () -> Unit,
    onNavigateToRegisterClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(prefillEmail) {
        if (!prefillEmail.isNullOrBlank()) {
            viewModel.onAction(LoginUiAction.PrefillEmailReceived(prefillEmail))
        }
    }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                LoginUiEffect.NavigateAfterLogin -> onNavigateAfterLogin()
            }
        }
    }

    HandleUiMessages(viewModel.uiMessages)

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToRegisterClick = {
            onNavigateToRegisterClick(state.email)
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}
