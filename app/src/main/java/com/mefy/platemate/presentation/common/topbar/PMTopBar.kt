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

@Composable
fun PMTopBar(
    config: PMTopBarConfig,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.pmColors.surface
) {
    when (config) {
        is PMTopBarConfig.Hidden -> {}
        is PMTopBarConfig.Standard -> PMStandardTopBar(config, modifier, containerColor)
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
                    size = dims.sizing.iconHuge
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
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = titleModifier
        )

        // Aksiyonlar — sağ kenar
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

@Composable
private fun PMTopBarPreviewContent() {
    val colors = MaterialTheme.pmColors
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
                    PMIconButton(onClick = {}) {
                        PMIcon(imageVector = Icons.Outlined.Share, tint = colors.textPrimary)
                    }
                    PMIconButton(onClick = {}) {
                        PMIcon(imageVector = Icons.Outlined.MoreVert, tint = colors.textPrimary)
                    }
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
