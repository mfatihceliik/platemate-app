package com.mefy.platemate.presentation.app.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.mefy.platemate.presentation.app.viewmodel.AppAuthViewModel
import com.mefy.platemate.presentation.navigation.AuthGraphDestination
import com.mefy.platemate.presentation.navigation.navigateToAuthAndClearBackStack

@Composable
internal fun AppAuthEffects(
    navController: NavHostController,
    viewModel: AppAuthViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated == false) {
            val destination = navController.currentDestination
            val route = destination?.route
            val isAlreadyInAuth = route == AuthGraphDestination::class.qualifiedName ||
                    destination?.hierarchy?.any { it.hasRoute(AuthGraphDestination::class) } == true

            if (!isAlreadyInAuth) {
                navController.navigateToAuthAndClearBackStack()
            }
        }
    }
}
