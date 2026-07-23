package com.mefy.platemate.presentation.app.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.banner.InAppBannerController
import com.mefy.platemate.presentation.common.banner.bannerFor
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.navigation.navigateToTopLevelDestination
import com.mefy.platemate.presentation.navigation.isTopLevelStartDestination
import com.mefy.platemate.presentation.navigation.toTopLevelDestinationOrNull

@Stable
class AppState(
    val navController: NavHostController,
    val bannerController: InAppBannerController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = currentDestination.toTopLevelDestinationOrNull()

    val showBottomBar: Boolean
        @Composable get() = currentDestination.isTopLevelStartDestination() && currentTopLevelDestination != null

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navController.navigateToTopLevelDestination(topLevelDestination)
    }

    fun showMessage(message: String, severity: BannerSeverity = BannerSeverity.Info) {
        bannerController.show(bannerFor(message, severity))
    }
}
