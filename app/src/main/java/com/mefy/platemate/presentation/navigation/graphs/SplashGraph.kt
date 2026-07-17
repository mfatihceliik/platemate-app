package com.mefy.platemate.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import com.mefy.platemate.presentation.features.splash.SplashRoute
import com.mefy.platemate.presentation.features.splash.SplashViewModel
import com.mefy.platemate.presentation.navigation.SplashDestination

internal fun NavGraphBuilder.splashGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable<SplashDestination> {
        SplashRoute(
            viewModel = hiltViewModel<SplashViewModel>(),
            onNavigateToAuth = onNavigateToAuth,
            onNavigateToMain = onNavigateToMain
        )
    }
}
