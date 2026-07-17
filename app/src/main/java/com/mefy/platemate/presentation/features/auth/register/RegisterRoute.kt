package com.mefy.platemate.presentation.features.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel,
    prefillIdentifier: String?,
    onNavigateAfterRegister: () -> Unit,
    onNavigateToLoginClick: (String?) -> Unit,
    onBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(prefillIdentifier) {
        if (!prefillIdentifier.isNullOrBlank()) {
            viewModel.onAction(RegisterUiAction.PrefillIdentifierReceived(prefillIdentifier))
        }
    }

    CollectRegisterUiEffect(
        effects = viewModel.uiEffect,
        onNavigateAfterRegister = onNavigateAfterRegister
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
