package com.mefy.platemate.presentation.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.common.messaging.CollectUiMessages
import com.mefy.platemate.presentation.common.dialog.DialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    prefillEmail: String?,
    onNavigateAfterLogin: () -> Unit,
    onNavigateToRegisterClick: (String) -> Unit,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (DialogModel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(prefillEmail) {
        if (!prefillEmail.isNullOrBlank()) {
            viewModel.onAction(LoginUiAction.PrefillEmailReceived(prefillEmail))
        }
    }

    CollectLoginUiEffect(
        effects = viewModel.uiEffect,
        onNavigateAfterLogin = onNavigateAfterLogin
    )

    CollectUiMessages(
        messages = viewModel.uiMessages,
        onShowSnackbar = onShowSnackbar,
        onShowDialog = onShowDialog
    )

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

@Composable
private fun CollectLoginUiEffect(
    effects: Flow<LoginUiEffect>,
    onNavigateAfterLogin: () -> Unit
) {
    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                LoginUiEffect.NavigateAfterLogin -> onNavigateAfterLogin()
            }
        }
    }
}
