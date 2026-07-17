package com.mefy.platemate.presentation.common.banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Üstten inen tek tip uygulama-içi banner: hem push bildirimleri hem hata/başarı UI mesajları.
 * [banner] null olunca gizlenir; her yeni banner'da [resetKey] değişerek alttaki geri-sayım
 * çizgisini (~[AUTO_DISMISS_MS]) yeniden başlatır ve süre dolunca [onDismiss] tetiklenir.
 *
 * Kapatma yolları: dokunma ([InAppBannerUiModel.onClick] + [onDismiss]), otomatik zaman aşımı,
 * ve **yukarı sürükleme** (eşiği geçince [onDismiss]; geçmezse yerine yaylanır).
 */
@Composable
fun PMInAppNotificationBanner(
    banner: InAppBannerUiModel?,
    resetKey: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Exit animasyonu sırasında içerik kalsın diye son gösterileni hatırla.
    var shown by remember { mutableStateOf<InAppBannerUiModel?>(null) }

    // Alttan dolan geri-sayım çizgisi (1f → 0f). Yeni banner'da yeniden başlar.
    val progress = remember { Animatable(1f) }

    LaunchedEffect(resetKey) {
        if (banner != null) {
            shown = banner
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(AUTO_DISMISS_MS.toInt(), easing = LinearEasing)
            )
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = banner != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
    ) {
        shown?.let { current ->
            BannerCard(
                banner = current,
                progress = progress.value,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun BannerCard(
    banner: InAppBannerUiModel,
    progress: Float,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    val shape = RoundedCornerShape(16.dp)
    val accent: Color = when (banner.severity) {
        BannerSeverity.Error -> colors.error
        BannerSeverity.Success -> colors.success
        BannerSeverity.Info -> colors.primary
    }

    val density = LocalDensity.current
    val dismissThresholdPx = with(density) { 56.dp.toPx() }
    val fadeDistancePx = with(density) { 140.dp.toPx() }

    // Yukarı sürükleme konumu (yalnız yukarı; ≤ 0). Yeni banner'da sıfırlanır.
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(banner) { offsetY.snapTo(0f) }

    // Sürükledikçe soluklaşsın: eşik/mesafe oranına göre alpha.
    val dragAlpha = 1f - (-offsetY.value / fadeDistancePx).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dims.spacing.s16)
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
            .graphicsLayer { alpha = dragAlpha }
            .shadow(elevation = 10.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
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
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Severity'yi belli eden accent sol kenar şeridi.
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(accent)
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(dims.spacing.s16),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
            ) {
                // İkon, düşük-alpha accent zeminli yuvarlak-kare kapta.
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accent.copy(alpha = 0.14f))
                        .padding(dims.spacing.s8),
                    contentAlignment = Alignment.Center
                ) {
                    PMIcon(
                        imageVector = banner.icon,
                        contentDescription = null,
                        tint = accent,
                        size = dims.sizing.iconMd
                    )
                }
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

        // Otomatik kapanma geri-sayımı: accent çizgi soldan kısalır.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(accent.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(accent)
            )
        }
    }
}

private const val AUTO_DISMISS_MS = 4000L
