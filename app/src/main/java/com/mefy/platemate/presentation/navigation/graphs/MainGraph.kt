package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation

internal fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    onNavigateToDiscoverDetail: (String) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
) {
    navigation<MainGraphDestination>(startDestination = SearchGraphDestination) {
        searchGraph(
            navController = navController,
            onNavigateToSearchDetail = onNavigateToSearchDetail,
        )
        discoverGraph(
            navController = navController,
            onNavigateToDiscoverDetail = onNavigateToDiscoverDetail,
        )
        messagesGraph(
            navController = navController,
        )
        profileGraph(
            navController = navController,
            onNavigateToSearchDetail = onNavigateToSearchDetail,
            onShowSnackbar = onShowSnackbar,
        )
        settingsGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
        )
        adminGraph(
            navController = navController,
        )
    }
}
