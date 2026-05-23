package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import com.mefy.platemate.presentation.features.auth.sessiongate.SessionGateRoute
import com.mefy.platemate.presentation.features.auth.sessiongate.SessionGateViewModel

internal fun NavGraphBuilder.sessionGateGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable<SessionGateDestination> {
        SessionGateRoute(
            viewModel = hiltViewModel<SessionGateViewModel>(),
            onNavigateToAuth = onNavigateToAuth,
            onNavigateToMain = onNavigateToMain
        )
    }
}
