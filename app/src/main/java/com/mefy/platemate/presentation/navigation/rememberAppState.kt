package com.mefy.platemate.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAppState(): AppState {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    return remember(navController, snackbarHostState, coroutineScope) {
        AppState(
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}
