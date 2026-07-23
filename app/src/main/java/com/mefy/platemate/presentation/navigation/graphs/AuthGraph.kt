package com.mefy.platemate.presentation.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.mefy.platemate.presentation.features.auth.login.LoginRoute
import com.mefy.platemate.presentation.features.auth.login.LoginViewModel
import com.mefy.platemate.presentation.features.auth.register.RegisterRoute
import com.mefy.platemate.presentation.features.auth.register.RegisterViewModel
import com.mefy.platemate.presentation.features.auth.onboarding.OnboardingRoute
import com.mefy.platemate.presentation.navigation.AuthGraphDestination
import com.mefy.platemate.presentation.navigation.LoginDestination
import com.mefy.platemate.presentation.navigation.OnboardingDestination
import com.mefy.platemate.presentation.navigation.RegisterDestination
import com.mefy.platemate.presentation.navigation.screenComposable

internal fun NavGraphBuilder.authGraph(
    onNavigateAfterLogin: () -> Unit,
    onNavigateAfterRegister: () -> Unit,
    onNavigateToRegister: (String?) -> Unit,
    onNavigateToLogin: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<AuthGraphDestination>(startDestination = OnboardingDestination) {
        composable<OnboardingDestination> {
            OnboardingRoute(
                onNavigateToRegister = { onNavigateToRegister(null) },
                onNavigateToLogin = { onNavigateToLogin(null) },
                modifier = modifier
            )
        }

        screenComposable<LoginDestination, LoginViewModel>(
            viewModel = { hiltViewModel<LoginViewModel>() },
        ) { viewModel ->
            val destination = backStackEntry.toRoute<LoginDestination>()
            LoginRoute(
                viewModel = viewModel,
                prefillEmail = destination.prefillIdentifier,
                onNavigateAfterLogin = onNavigateAfterLogin,
                onNavigateToRegisterClick = onNavigateToRegister,
                modifier = modifier
            )
        }

        screenComposable<RegisterDestination, RegisterViewModel>(
            viewModel = { hiltViewModel<RegisterViewModel>() },
        ) { viewModel ->
            val destination = backStackEntry.toRoute<RegisterDestination>()
            RegisterRoute(
                viewModel = viewModel,
                prefillIdentifier = destination.prefillIdentifier,
                onNavigateAfterRegister = onNavigateAfterRegister,
                onNavigateToLoginClick = onNavigateToLogin,
                modifier = modifier
            )
        }
    }
}
