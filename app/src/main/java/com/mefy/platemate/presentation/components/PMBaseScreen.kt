package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PMBaseScreen(
    modifier: Modifier = Modifier,
    topBarConfig: PMTopBarConfig = PMTopBarConfig.Hidden,
    containerColor: Color = MaterialTheme.colorScheme.background,
    topBarContainerColor: Color = containerColor,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = containerColor,
        topBar = {
            PMTopBar(
                config = topBarConfig,
                containerColor = topBarContainerColor
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        content(innerPadding)
    }
}
