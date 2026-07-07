package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.banner.InAppBannerController
import com.mefy.platemate.presentation.common.banner.bannerFor
import kotlin.reflect.KClass

@Stable
class AppState(
    val navController: NavHostController,
    val bannerController: InAppBannerController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navController.navigateToTopLevelDestination(topLevelDestination)
    }

    /** Üstten inen banner ile kısa mesaj gösterir (eski snackbar yerine). */
    fun showMessage(message: String, severity: BannerSeverity = BannerSeverity.Info) {
        bannerController.show(bannerFor(message, severity))
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
