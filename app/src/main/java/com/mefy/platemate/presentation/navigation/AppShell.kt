package com.mefy.platemate.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.common.messaging.LocalUiMessageHandlers
import com.mefy.platemate.presentation.common.messaging.UiMessageHandlers
import com.mefy.platemate.presentation.components.LocalIsOnline
import com.mefy.platemate.presentation.components.LocalScaffoldPadding
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Uygulama chrome'u (navigasyon DEĞİL): tek [Scaffold] (edge-to-edge), snackbar host,
 * çevrimdışı bandı, yüzen [MainBottomBar] overlay'i, ekran ağacına dağıtılan composition
 * local'lar ([LocalScaffoldPadding]/[LocalIsOnline]/[LocalUiMessageHandlers]) ve [DialogHost].
 *
 * [content] = ekran içeriği (genelde [AppNavHost]). İçerik [LocalScaffoldPadding]'i
 * PMBaseScreen üzerinden okuyup yüzen alt bar için boşluk bırakır.
 */
@Composable
fun AppShell(
    modifier: Modifier = Modifier,
    isOnline: Boolean,
    uiMessageHandlers: UiMessageHandlers,
    dialogHostState: DialogHostState,
    showBottomBar: Boolean,
    selectedTopLevelDestination: TopLevelDestination?,
    onTopLevelDestinationSelected: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        // Inset yönetimi alt bileşenlere devredilir (PMTopBar status-bar, MainBottomBar nav-bar);
        // içerik edge-to-edge çizebilir. Geri-bildirim üstten inen banner ile (PlateMateAppRoot).
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        // MainBottomBar Scaffold slotu değil; içeriğin üstüne overlay edilir (Box). Böylece
        // ekran zemini bar bölgesinde sürekli olur ve floating pill etrafında opak blok kalmaz.
        // Ekranlar LocalScaffoldPadding ile pill üstüne kadar boşluk bırakır.
        val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val dims = MaterialTheme.pmDimensions
        val scaffoldPadding = if (showBottomBar) {
            PaddingValues(bottom = dims.sizing.mainBottomBarHeight + navBottom)
        } else {
            PaddingValues(dims.spacing.s0)
        }

        CompositionLocalProvider(
            LocalScaffoldPadding provides scaffoldPadding,
            LocalIsOnline provides isOnline,
            LocalUiMessageHandlers provides uiMessageHandlers
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    OfflineBanner(visible = !isOnline)
                    Box(modifier = Modifier.weight(1f)) {
                        content()
                    }
                }

                // Bar tek karede yok olmasın; ekran geçişiyle (NAV_TRANSITION_DURATION_MS)
                // senkron aşağı süzülerek çıksın / yukarı süzülerek girsin.
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
                    // Çıkış animasyonu sırasında seçim null'a düşebilir (ör. logout) → son değeri tut.
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
