package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateToAuthGraphFromGate() {
    navigate(AuthGraphDestination) {
        popUpTo<SessionGateDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToMainGraphFromGate() {
    navigate(MainGraphDestination) {
        popUpTo<SessionGateDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToMainAndClearBackStack() {
    navigate(MainGraphDestination) {
        popUpTo<AuthGraphDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToAuthAndClearBackStack() {
    navigate(AuthGraphDestination) {
        popUpTo(graph.findStartDestination().id) { inclusive = true }
        launchSingleTop = true
    }
}


fun NavHostController.navigateToLogin(prefillIdentifier: String? = null) {
    navigate(LoginDestination(prefillIdentifier = prefillIdentifier))
}

fun NavHostController.navigateToRegister(prefillIdentifier: String? = null) {
    navigate(RegisterDestination(prefillIdentifier = prefillIdentifier))
}

fun NavHostController.navigateToDiscoverDetail(id: String) {
    navigate(DiscoverDetailDestination(id = id))
}

fun NavHostController.navigateToSearchDetail(id: String) {
    navigate(SearchDetailDestination(id = id))
}

fun NavHostController.navigateToTopLevelDestination(destination: TopLevelDestination) {
    navigate(destination.graphDestination) {
        popUpTo(MainGraphDestination) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
