package com.mefy.platemate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.mefy.platemate.presentation.common.banner.InAppBannerController

@Composable
fun rememberAppState(): AppState {
    val navController = rememberNavController()
    val bannerController = remember { InAppBannerController() }

    return remember(navController, bannerController) {
        AppState(
            navController = navController,
            bannerController = bannerController
        )
    }
}
