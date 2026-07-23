package com.mefy.platemate.presentation.common.banner

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMIconContainer
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PMBannerCard(
    banner: InAppBannerUiModel,
    progress: Float,
    onDismiss: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val shapes = PMTheme.shapes

    val accent: Color = when (banner.severity) {
        BannerSeverity.Error -> colors.error
        BannerSeverity.Success -> colors.success
        BannerSeverity.Info -> colors.primary
    }

    val density = LocalDensity.current

    val dismissThresholdPx = remember(density) {
        with(density) { 56.dp.toPx() }
    }

    val fadeDistancePx = remember(density) {
        with(density) { 140.dp.toPx() }
    }

    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(banner) { offsetY.snapTo(0f) }

    val dragAlpha by remember {
        derivedStateOf {
            1f - (-offsetY.value / fadeDistancePx)
                .coerceIn(0f, 1f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.s16)
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
            .graphicsLayer { alpha = dragAlpha }
            .shadow(elevation = 10.dp, shape = shapes.medium)
            .clip(shapes.medium)
            .background(colors.surface)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetY.snapTo((offsetY.value + dragAmount).coerceAtMost(0f))
                        }
                    },
                    onDragEnd = {
                        if (offsetY.value < -dismissThresholdPx) {
                            onDismiss()
                        } else {
                            scope.launch { offsetY.animateTo(0f) }
                        }
                    }
                )
            }
            .clickable {
                banner.onClick?.invoke()
                onDismiss()
            }
    ) {
        Row(
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        color = accent,
                        size = Size(spacing.s8.toPx(), size.height)
                    )
                }
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(spacing.s16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.s12)
            ) {

                PMIconContainer(
                    imageVector = banner.icon,
                    tint = accent,
                    iconSize = sizing.iconLg,
                    containerSize = sizing.iconXl,
                    containerColor = accent.copy(alpha = 0.2f)
                )
                Column(modifier = Modifier.weight(1f)) {
                    if (!banner.title.isNullOrBlank()) {
                        PMText(
                            text = banner.title,
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    PMText(
                        text = banner.message,
                        style = if (banner.title.isNullOrBlank()) PMTextStyle.Body else PMTextStyle.Caption,
                        color = if (banner.title.isNullOrBlank()) colors.textPrimary else colors.textTertiary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sizing.bannerProgressHeight)
                .drawWithCache {
                    val background = accent.copy(alpha = 0.12f)
                    onDrawBehind {
                        drawRect(background)
                        drawRect(
                            color = accent,
                            size = Size(size.width * progress.coerceIn(0f, 1f), size.height)
                        )
                    }
                }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardSuccessPreview() {
    PlateMateTheme(darkTheme = false) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "İşlem Başarılı",
                message = "Profil bilgileriniz başarıyla güncellendi.",
                icon = Icons.Default.CheckCircle,
                severity = BannerSeverity.Success
            ),
            progress = 0.5f,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardErrorPreview() {
    PlateMateTheme(darkTheme = false) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "Bağlantı Hatası",
                message = "Sunucuya ulaşılamıyor. Lütfen internet bağlantınızı kontrol edip tekrar deneyin.",
                icon = Icons.Default.Warning,
                severity = BannerSeverity.Error
            ),
            progress = 0.8f,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardInfoPreview() {
    PlateMateTheme(darkTheme = false) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "Yeni Bildirim",
                message = "Mehmet Yılmaz plakanıza bir yorum yaptı.",
                icon = Icons.Default.Info,
                severity = BannerSeverity.Info
            ),
            progress = 0.3f,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardSuccessDarkPreview() {
    PlateMateTheme(darkTheme = true) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "İşlem Başarılı",
                message = "Profil bilgileriniz başarıyla güncellendi.",
                icon = Icons.Default.CheckCircle,
                severity = BannerSeverity.Success
            ),
            progress = 0.5f,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardErrorDarkPreview() {
    PlateMateTheme(darkTheme = true) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "Bağlantı Hatası",
                message = "Sunucuya ulaşılamıyor. Lütfen internet bağlantınızı kontrol edip tekrar deneyin.",
                icon = Icons.Default.Warning,
                severity = BannerSeverity.Error
            ),
            progress = 0.8f,
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBannerCardInfoDarkPreview() {
    PlateMateTheme(darkTheme = true) {
        PMBannerCard(
            banner = InAppBannerUiModel(
                title = "Yeni Bildirim",
                message = "Mehmet Yılmaz plakanıza bir yorum yaptı.",
                icon = Icons.Default.Info,
                severity = BannerSeverity.Info
            ),
            progress = 0.3f,
            onDismiss = {}
        )
    }
}
