package com.mefy.platemate.presentation.common.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMTopBar(
    modifier: Modifier = Modifier,
    config: PMTopBarConfig,
    containerColor: Color = MaterialTheme.pmColors.background,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    when (config) {
        is PMTopBarConfig.Hidden -> {}
        is PMTopBarConfig.Standard -> PMStandardTopBar(config, modifier, containerColor)
        is PMTopBarConfig.Transparent -> PMTransparentTopBar(config, modifier, scrollBehavior)
        // Status-bar inset + zemin burada merkezî olarak uygulanır; Custom topbar'lar
        // (ör. ConversationTopBar) kendi inset'ini yönetmek zorunda kalmaz.
        is PMTopBarConfig.Custom -> Box(
            modifier = modifier
                .fillMaxWidth()
                .background(containerColor)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            config.content()
        }
    }
}

@Composable
private fun PMStandardTopBar(
    config: PMTopBarConfig.Standard,
    modifier: Modifier,
    containerColor: Color
) {
    val dims = MaterialTheme.pmDimensions
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(dims.sizing.topBarHeight)
            .padding(horizontal = dims.spacing.s4)
    ) {
        config.onBackClick?.let { onBack ->
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                PMIcon(
                    modifier = Modifier.debouncedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onBack
                    ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    size = dims.sizing.iconLg
                )
            }
        }

        val titleModifier = when (config.alignment) {
            PMTopBarAlignment.Center -> Modifier
                .align(Alignment.Center)
                .padding(horizontal = dims.spacing.s48)

            PMTopBarAlignment.Start -> Modifier
                .align(Alignment.CenterStart)
                .padding(
                    start = if (config.onBackClick != null) dims.spacing.s48 else dims.spacing.s12,
                    end = dims.spacing.s48
                )
        }
        PMText(
            text = config.title,
            fontSize = dims.fontSize.xxl,
            overflow = TextOverflow.Ellipsis,
            modifier = titleModifier,
            maxLines = 1

        )

        // Aksiyonlar — sağ kenar
        config.actions?.let { actions ->
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PMTransparentTopBar(
    config: PMTopBarConfig.Transparent,
    modifier: Modifier,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    val dims = MaterialTheme.pmDimensions
    val interactionSource = remember { MutableInteractionSource() }

    // Set up scroll limit if behavior is present
    if (scrollBehavior != null) {
        val density = androidx.compose.ui.platform.LocalDensity.current
        val heightOffsetLimitPx = with(density) { -(dims.sizing.topBarHeight + WindowInsets.statusBars.getTop(density).toDp()).toPx() }
        androidx.compose.runtime.SideEffect {
            if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimitPx) {
                scrollBehavior.state.heightOffsetLimit = heightOffsetLimitPx
            }
        }
    }

    val fraction = scrollBehavior?.state?.collapsedFraction ?: 0f
    val offset = scrollBehavior?.state?.heightOffset ?: 0f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                if (config.hideOnScroll && scrollBehavior != null) {
                    translationY = offset
                    alpha = 1f - fraction
                }
            }
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(dims.sizing.topBarHeight)
            .padding(horizontal = dims.spacing.s4)
    ) {
        config.onBackClick?.let { onBack ->
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                PMIcon(
                    modifier = Modifier.debouncedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onBack
                    ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    size = dims.sizing.iconLg
                )
            }
        }

        if (config.title != null) {
            PMText(
                text = config.title,
                fontSize = dims.fontSize.xxl,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = dims.spacing.s48),
                maxLines = 1
            )
        }

        config.actions?.let { actions ->
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}

// ── Previews ────────────────────────────────────────────────

@Preview(name = "PMTopBar Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTopBarLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTopBarPreviewContent()
    }
}

@Preview(name = "PMTopBar Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMTopBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTopBarPreviewContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PMTopBarPreviewContent() {
    Column {
        // Geri butonsuz, ortalı (tab ekranı)
        PMTopBar(config = PMTopBarConfig.Standard(title = "PlateMate"))
        // Geri butonlu, ortalı
        PMTopBar(
            config = PMTopBarConfig.Standard(
                title = "Profil",
                onBackClick = {}
            )
        )
        // Geri + aksiyonlar, ortalı
        PMTopBar(
            config = PMTopBarConfig.Standard(
                title = "Plaka Detayı",
                onBackClick = {},
                actions = {
                    PMIconButton(onClick = {}, imageVector = Icons.Outlined.Share)
                    PMIconButton(onClick = {}, imageVector = Icons.Outlined.MoreVert)
                }
            )
        )
        // Sola hizalı başlık
        PMTopBar(
            config = PMTopBarConfig.Standard(
                title = "Sola Hizalı",
                onBackClick = {},
                alignment = PMTopBarAlignment.Start
            )
        )
    }
}
