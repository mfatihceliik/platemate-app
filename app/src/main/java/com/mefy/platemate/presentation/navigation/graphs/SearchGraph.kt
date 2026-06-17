package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailRoute
import com.mefy.platemate.presentation.features.main.platedetail.PlateDetailViewModel
import com.mefy.platemate.presentation.features.main.review.ReviewRoute
import com.mefy.platemate.presentation.features.main.review.ReviewViewModel
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

        composable<SearchDetailDestination> {
            PlateDetailRoute(
                viewModel = hiltViewModel<PlateDetailViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onNavigateToReview = { navController.navigateToReview(it) },
                modifier = modifier
            )
        }

        composable<ReviewDestination> {
            ReviewRoute(
                viewModel = hiltViewModel<ReviewViewModel>(),
                onNavigateBack = { navController.popBackStack() },
                onReviewSubmitted = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
