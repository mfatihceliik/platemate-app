package com.mefy.platemate.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation

internal fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    onNavigateToDiscoverDetail: (String) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<MainGraphDestination>(startDestination = SearchGraphDestination) {
        searchGraph(
            navController = navController,
            onNavigateToSearchDetail = onNavigateToSearchDetail,
            modifier = modifier
        )
        discoverGraph(
            navController = navController,
            onNavigateToDiscoverDetail = onNavigateToDiscoverDetail,
            modifier = modifier
        )
        messagesGraph(navController = navController, modifier = modifier)
        profileGraph(
            navController = navController,
            onNavigateToSearchDetail = onNavigateToSearchDetail,
            onShowSnackbar = onShowSnackbar,
            modifier = modifier
        )
        settingsGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
            modifier = modifier
        )
    }
}
