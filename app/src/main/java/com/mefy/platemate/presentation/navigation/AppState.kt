package com.mefy.platemate.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Stable
class AppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = currentDestination.toTopLevelDestinationOrNull()

    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination.isTopLevelStartDestination()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navController.navigateToTopLevelDestination(topLevelDestination)
    }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
}

internal fun NavDestination?.toTopLevelDestinationOrNull(): TopLevelDestination? {
    if (this == null) return null

    return TopLevelDestination.entries.firstOrNull { destination ->
        hierarchy.any { navDestination ->
            navDestination.hasRoute(destination.route) || navDestination.hasRoute(destination.graphRoute)
        }
    }
}

internal fun NavDestination?.isTopLevelStartDestination(): Boolean {
    if (this == null) return false

    return TopLevelDestination.entries.any { destination ->
        hasRoute(destination.route)
    }
}

internal fun KClass<out AppDestination>?.toTopLevelDestinationOrNull(): TopLevelDestination? {
    return TopLevelDestination.entries.firstOrNull { destination ->
        destination.route == this
    }
}

internal fun KClass<out AppDestination>?.shouldShowBottomBarForRouteClass(): Boolean {
    return toTopLevelDestinationOrNull() != null
}
