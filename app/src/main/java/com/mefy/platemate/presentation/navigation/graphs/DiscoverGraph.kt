package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.discover.DiscoverRoute
import com.mefy.platemate.presentation.features.main.discover.DiscoverViewModel
import com.mefy.platemate.presentation.features.main.MainPlaceholderScreen

internal fun NavGraphBuilder.discoverGraph(
    navController: NavHostController,
    onNavigateToDiscoverDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<DiscoverGraphDestination>(startDestination = DiscoverDestination) {
        composable<DiscoverDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            DiscoverRoute(
                viewModel = hiltViewModel<DiscoverViewModel>(parentEntry),
                onNavigateToTrendDetail = onNavigateToDiscoverDetail,
                modifier = modifier
            )
        }

        // TODO: Add typed deep links if/when this detail screen is implemented.
        composable<DiscoverDetailDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_discover_detail_placeholder_title,
                modifier = modifier
            )
        }
    }
}
