package com.mefy.platemate.presentation.common.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMTopBar(
    config: PMTopBarConfig,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.pmColors.surface
) {
    val dims = MaterialTheme.pmDimensions

    when (config) {
        is PMTopBarConfig.Hidden -> {}
        is PMTopBarConfig.Standard -> {
            TopAppBar(
                title = {
                    PMText(
                        text = config.title,
                        fontSize = dims.fontSize.xxl
                    )
                },
                navigationIcon = { config.onBackClick?.let { PMBackButton(it) } },
                actions = { config.actions?.invoke(this) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor),
                modifier = modifier
            )
        }
        is PMTopBarConfig.Collapsing -> {
            LargeTopAppBar(
                title = {
                    PMText(
                        text = config.title,
                        fontSize = dims.fontSize.xxl
                    )
                },
                navigationIcon = { config.onBackClick?.let { PMBackButton(it) } },
                actions = { config.actions?.invoke(this) },
                scrollBehavior = config.scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = containerColor),
                modifier = modifier
            )
        }
        is PMTopBarConfig.Transparent -> {
            TopAppBar(
                title = {},
                navigationIcon = { config.onBackClick?.let { PMBackButton(it) } },
                actions = { config.actions?.invoke(this) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = modifier
            )
        }
        is PMTopBarConfig.Custom -> {
            config.content()
        }
    }
}

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

@Composable
private fun PMTopBarPreviewContent() {
    val colors = MaterialTheme.pmColors
    Column {
        // Geri butonsuz (tab ekranı)
        PMTopBar(config = PMTopBarConfig.Standard(title = "PlateMate"))
        // Geri butonlu
        PMTopBar(config = PMTopBarConfig.Standard(
            title = "Profil",
            onBackClick = {}
        ))
        // Geri + aksiyonlar
        PMTopBar(config = PMTopBarConfig.Standard(
            title = "Plaka Detayı",
            onBackClick = {},
            actions = {
                IconButton(onClick = {}) {
                    PMIcon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        tint = colors.textPrimary
                    )
                }
                IconButton(onClick = {}) {
                    PMIcon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                        tint = colors.textPrimary
                    )
                }
            }
        ))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "PMTopBar Collapsing Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTopBarCollapsingLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTopBar(
            config = PMTopBarConfig.Collapsing(
                title = "Profil",
                onBackClick = {},
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "PMTopBar Collapsing Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMTopBarCollapsingDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTopBar(
            config = PMTopBarConfig.Collapsing(
                title = "Profil",
                onBackClick = {},
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            )
        )
    }
}

@Preview(name = "PMTopBar Transparent Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMTopBarTransparentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMTopBar(config = PMTopBarConfig.Transparent(onBackClick = {}))
    }
}

@Preview(name = "PMTopBar Transparent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMTopBarTransparentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMTopBar(config = PMTopBarConfig.Transparent(onBackClick = {}))
    }
}
