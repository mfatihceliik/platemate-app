package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.mefy.platemate.presentation.common.topbar.PMTopBar
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.theme.pmColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBaseScreen(
    modifier: Modifier = Modifier,
    topBarConfig: PMTopBarConfig = PMTopBarConfig.Hidden,
    containerColor: Color = MaterialTheme.pmColors.background,
    topBarContainerColor: Color = containerColor,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldPadding = LocalScaffoldPadding.current
    val scrollModifier = when (val cfg = topBarConfig) {
        is PMTopBarConfig.Collapsing -> Modifier.nestedScroll(cfg.scrollBehavior.nestedScrollConnection)
        else -> Modifier
    }

    val base = modifier
        .fillMaxSize()
        .background(containerColor)
        .then(scrollModifier)

    if (topBarConfig is PMTopBarConfig.Transparent) {
        Box(base) {
            content(scaffoldPadding)
            PMTopBar(config = topBarConfig, containerColor = topBarContainerColor)
        }
    } else {
        Column(base) {
            PMTopBar(config = topBarConfig, containerColor = topBarContainerColor)
            Box(
                Modifier
                    .weight(1f)
                    .then(
                        if (topBarConfig is PMTopBarConfig.Hidden)
                            Modifier.windowInsetsPadding(WindowInsets.statusBars)
                        else Modifier
                    )
            ) {
                content(scaffoldPadding)
            }
            bottomBar()
        }
    }
}
