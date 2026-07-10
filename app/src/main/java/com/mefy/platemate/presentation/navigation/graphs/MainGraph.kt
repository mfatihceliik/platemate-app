package com.mefy.platemate.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.navigation.MainGraphDestination
import com.mefy.platemate.presentation.navigation.SearchGraphDestination
import com.mefy.platemate.presentation.navigation.adminGraph
import com.mefy.platemate.presentation.navigation.discoverGraph
import com.mefy.platemate.presentation.navigation.messagesGraph
import com.mefy.platemate.presentation.navigation.settingsGraph

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
