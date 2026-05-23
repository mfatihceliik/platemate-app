package com.mefy.platemate.presentation.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.common.event.CollectCommonUiEvents
import com.mefy.platemate.presentation.common.event.CommonDialogModel
import com.mefy.platemate.presentation.common.text.UiText
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    prefillEmail: String?,
    onNavigateAfterLogin: () -> Unit,
    onNavigateToRegisterClick: (String) -> Unit,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (CommonDialogModel) -> Unit,
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

    CollectCommonUiEvents(
        events = viewModel.commonUiEvents,
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
