package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.MainPlaceholderScreen
import com.mefy.platemate.presentation.features.main.search.SearchRoute
import com.mefy.platemate.presentation.features.main.search.SearchViewModel

internal fun NavGraphBuilder.searchGraph(
    navController: NavHostController,
    onNavigateToSearchDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    navigation<SearchGraphDestination>(startDestination = SearchDestination) {
        composable<SearchDestination> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainGraphDestination)
            }
            SearchRoute(
                viewModel = hiltViewModel<SearchViewModel>(parentEntry),
                onNavigateToSearchDetail = onNavigateToSearchDetail,
                modifier = modifier
            )
        }

        // TODO: Add typed deep links if/when this detail screen is implemented.
        composable<SearchDetailDestination> {
            MainPlaceholderScreen(
                titleRes = R.string.main_search_placeholder_title,
                modifier = modifier
            )
        }
    }
}
