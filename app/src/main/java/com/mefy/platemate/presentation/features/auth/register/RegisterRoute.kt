package com.mefy.platemate.presentation.features.auth.register

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
fun RegisterRoute(
    viewModel: RegisterViewModel,
    prefillIdentifier: String?,
    onNavigateAfterRegister: () -> Unit,
    onNavigateToLoginClick: (String?) -> Unit,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (CommonDialogModel) -> Unit,
    onBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(prefillIdentifier) {
        if (!prefillIdentifier.isNullOrBlank()) {
            viewModel.onAction(RegisterUiAction.PrefillIdentifierReceived(prefillIdentifier))
        }
    }

    CollectRegisterUiEffect(
        effects = viewModel.uiEffect,
        onNavigateAfterRegister = onNavigateAfterRegister
    )

    CollectCommonUiEvents(
        events = viewModel.commonUiEvents,
        onShowSnackbar = onShowSnackbar,
        onShowDialog = onShowDialog
    )

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToLoginClick = {
            onNavigateToLoginClick(state.email.takeIf { it.isNotBlank() })
        },
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
private fun CollectRegisterUiEffect(
    effects: Flow<RegisterUiEffect>,
    onNavigateAfterRegister: () -> Unit
) {
    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                RegisterUiEffect.NavigateAfterRegister -> onNavigateAfterRegister()
            }
        }
    }
}
