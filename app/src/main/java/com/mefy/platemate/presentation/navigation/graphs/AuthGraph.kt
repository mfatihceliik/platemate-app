package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.mefy.platemate.presentation.common.event.CommonDialogModel
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.auth.login.LoginRoute
import com.mefy.platemate.presentation.features.auth.login.LoginViewModel
import com.mefy.platemate.presentation.features.auth.register.RegisterRoute
import com.mefy.platemate.presentation.features.auth.register.RegisterViewModel
import com.mefy.platemate.presentation.features.auth.onboarding.OnboardingRoute

internal fun NavGraphBuilder.authGraph(
    onNavigateAfterLogin: () -> Unit,
    onNavigateAfterRegister: () -> Unit,
    onNavigateToRegister: (String?) -> Unit,
    onNavigateToLogin: (String?) -> Unit,
    onShowSnackbar: (UiText) -> Unit,
    onShowDialog: (CommonDialogModel) -> Unit,
    onBackClick: () -> Unit,
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

        composable<LoginDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<LoginDestination>()
            val viewModel = hiltViewModel<LoginViewModel>()

            LoginRoute(
                viewModel = viewModel,
                prefillEmail = destination.prefillIdentifier,
                onNavigateAfterLogin = onNavigateAfterLogin,
                onNavigateToRegisterClick = onNavigateToRegister,
                onShowSnackbar = onShowSnackbar,
                onShowDialog = onShowDialog,
                onBackClick = onBackClick,
                modifier = modifier
            )
        }

        composable<RegisterDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<RegisterDestination>()
            val viewModel = hiltViewModel<RegisterViewModel>()

            RegisterRoute(
                viewModel = viewModel,
                prefillIdentifier = destination.prefillIdentifier,
                onNavigateAfterRegister = onNavigateAfterRegister,
                onNavigateToLoginClick = onNavigateToLogin,
                onShowSnackbar = onShowSnackbar,
                onShowDialog = onShowDialog,
                onBackClick = onBackClick,
                modifier = modifier
            )
        }
    }
}
