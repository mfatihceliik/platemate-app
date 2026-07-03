package com.mefy.platemate.presentation.features.auth.sessiongate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SessionGateRoute(
    viewModel: SessionGateViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.target) {
        when (state.target) {
            SessionGateTarget.Auth -> onNavigateToAuth()
            SessionGateTarget.Main -> onNavigateToMain()
            null -> Unit
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
