package com.mefy.platemate.presentation.common.basescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.app.providers.LocalNetworkState
import com.mefy.platemate.presentation.app.providers.LocalScaffoldPadding
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.topbar.PMTopBar
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.util.plus
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBaseScreen(
    modifier: Modifier = Modifier,
    topBarConfig: PMTopBarConfig = PMTopBarConfig.Hidden,
    viewModel: BaseViewModel? = null,
    containerColor: Color = PMTheme.colors.background,
    topBarContainerColor: Color = PMTheme.colors.background,
    applyImePadding: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    contentBottomOverlay: @Composable BoxScope.() -> Unit = {},
    status: ScreenStatus? = null,
    onRetry: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(PMTheme.spacing.s8),
    keepTopBarWhileLoading: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    loading: @Composable (PaddingValues) -> Unit = {},
    empty: @Composable (PaddingValues) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val finalOnRetry = onRetry ?: if (viewModel != null) { { viewModel.onRetry() } } else null
    val finalOnRefresh = onRefresh ?: if (viewModel != null) { { viewModel.onRefresh() } } else null

    val scaffoldPadding = LocalScaffoldPadding.current
    val isOnline = LocalNetworkState.current
    val spacing = PMTheme.spacing

    val layoutDirection = LocalLayoutDirection.current
    val mergedPadding = remember(scaffoldPadding, contentPadding, layoutDirection, spacing.s24) {
        val merged = scaffoldPadding.plus(contentPadding, layoutDirection)
        val extraBottomBarGap = if (scaffoldPadding.calculateBottomPadding() > 0.dp) spacing.s24 else spacing.s0
        PaddingValues(
            start = merged.calculateStartPadding(layoutDirection),
            top = merged.calculateTopPadding(),
            end = merged.calculateEndPadding(layoutDirection),
            bottom = merged.calculateBottomPadding() + extraBottomBarGap
        )
    }
    val renderContent: @Composable (PaddingValues) -> Unit = { padding ->
        if (finalOnRefresh != null) {
            PMPullToRefresh(isRefreshing = isRefreshing, onRefresh = { finalOnRefresh.invoke() }) {
                content(padding)
            }
        } else {
            content(padding)
        }
    }

    val effectiveStatus = if (status != null && !isOnline) {
        ScreenStatus.Error(UiText.Resource(R.string.common_error_network))
    } else {
        status
    }

    val hideChrome = (effectiveStatus == ScreenStatus.Loading && !keepTopBarWhileLoading) ||
            effectiveStatus is ScreenStatus.Error

    val scrollBehavior = if (topBarConfig is PMTopBarConfig.Transparent) TopAppBarDefaults.enterAlwaysScrollBehavior() else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor)
            .then(
                if (applyImePadding) {
                    Modifier.windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars))
                } else {
                    Modifier
                }
            )
            .then(
                if (scrollBehavior != null) {
                    Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    Modifier
                }
            )
    ) {
        if (!hideChrome && topBarConfig !is PMTopBarConfig.Transparent) {
            PMTopBar(config = topBarConfig, containerColor = topBarContainerColor)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .then(
                    if (hideChrome || topBarConfig is PMTopBarConfig.Hidden)
                        Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    else Modifier
                )
        ) {
            if (effectiveStatus == null) {
                renderContent(mergedPadding)
            } else {
                BaseScreenStatusContent(
                    status = effectiveStatus,
                    padding = mergedPadding,
                    onRetry = { finalOnRetry?.invoke() },
                    loading = loading,
                    empty = empty,
                    content = renderContent
                )
            }
            if (!hideChrome) {
                contentBottomOverlay()
            }

            if (!hideChrome && topBarConfig is PMTopBarConfig.Transparent) {
                PMTopBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    config = topBarConfig,
                    scrollBehavior = scrollBehavior
                )
            }
        }
        if (!hideChrome) {
            bottomBar()
        }
    }
}
