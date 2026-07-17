package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlin.reflect.KClass

/**
 * [NavDestination] / rota sınıfı → [TopLevelDestination] eşlemesi ve alt-bar görünürlük kuralları.
 * Navigasyon-domaini yardımcıları; hem [NavTransitions] hem uygulama chrome'u
 * ([com.mefy.platemate.presentation.app.PlateMateAppRoot]) bunlara dayanır.
 */
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
