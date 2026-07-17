package com.mefy.platemate.presentation.app

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
