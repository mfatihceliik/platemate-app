package com.mefy.platemate.presentation.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.common.bottombar.MainBottomBar
import com.mefy.platemate.presentation.common.connectivity.OfflineBanner
import com.mefy.platemate.presentation.common.dialog.DialogHost
import com.mefy.platemate.presentation.common.dialog.DialogHostState
import com.mefy.platemate.presentation.app.providers.LocalNetworkState
import com.mefy.platemate.presentation.app.providers.LocalScaffoldPadding
import com.mefy.platemate.presentation.app.providers.LocalUiMessageHandlers
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers
import com.mefy.platemate.presentation.navigation.NAV_TRANSITION_DURATION_MS
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    networkState: Boolean,
    uiMessageHandlers: UiMessageHandlers,
    dialogHostState: DialogHostState,
    showBottomBar: Boolean,
    selectedTopLevelDestination: TopLevelDestination?,
    onTopLevelDestinationSelected: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val sizing = PMTheme.sizing
        val spacing = PMTheme.spacing
        val targetBottomPadding = if (showBottomBar) sizing.bottomBarHeight + navBottom else navBottom
        val animatedBottomPadding by animateDpAsState(
            targetValue = targetBottomPadding,
            animationSpec = tween(NAV_TRANSITION_DURATION_MS, easing = FastOutSlowInEasing),
            label = "scaffoldBottomPadding"
        )
        val scaffoldPadding = PaddingValues(bottom = animatedBottomPadding)

        CompositionLocalProvider(
            LocalScaffoldPadding provides scaffoldPadding,
            LocalNetworkState provides networkState,
            LocalUiMessageHandlers provides uiMessageHandlers
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    OfflineBanner(visible = !networkState)
                    Box(modifier = Modifier.weight(1f)) {
                        content()
                    }
                }

                AnimatedVisibility(
                    visible = showBottomBar && selectedTopLevelDestination != null,
                    enter = slideInVertically(
                        tween(NAV_TRANSITION_DURATION_MS, easing = FastOutSlowInEasing)
                    ) { it } + fadeIn(tween(NAV_TRANSITION_DURATION_MS)),
                    exit = slideOutVertically(
                        tween(NAV_TRANSITION_DURATION_MS, easing = FastOutSlowInEasing)
                    ) { it } + fadeOut(tween(NAV_TRANSITION_DURATION_MS)),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    var lastTab by remember { mutableStateOf(selectedTopLevelDestination) }
                    if (selectedTopLevelDestination != null) lastTab = selectedTopLevelDestination
                    lastTab?.let { tab ->
                        MainBottomBar(
                            selectedDestination = tab,
                            onDestinationSelected = onTopLevelDestinationSelected
                        )
                    }
                }
            }
            DialogHost(state = dialogHostState)
        }
    }
}
