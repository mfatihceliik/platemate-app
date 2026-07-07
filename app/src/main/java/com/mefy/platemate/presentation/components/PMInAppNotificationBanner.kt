package com.mefy.platemate.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.common.banner.BannerSeverity
import com.mefy.platemate.presentation.common.banner.InAppBannerUiModel
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import kotlinx.coroutines.delay

/**
 * Üstten inen tek tip uygulama-içi banner: hem push bildirimleri hem hata/başarı UI mesajları.
 * [banner] null olunca gizlenir; her yeni banner'da [resetKey] değişerek ~[AUTO_DISMISS_MS] sonra
 * otomatik kapanmayı yeniden tetikler. Dokunma [InAppBannerUiModel.onClick]'i (varsa) + [onDismiss],
 * zaman aşımı [onDismiss]'i çağırır.
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

    LaunchedEffect(resetKey) {
        if (banner != null) {
            shown = banner
            delay(AUTO_DISMISS_MS)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = banner != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
    ) {
        shown?.let { current -> BannerCard(banner = current, onDismiss = onDismiss) }
    }
}

@Composable
private fun BannerCard(
    banner: InAppBannerUiModel,
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dims.spacing.s16)
            .shadow(elevation = 8.dp, shape = shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                banner.onClick?.invoke()
                onDismiss()
            }
            .padding(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        PMIcon(
            imageVector = banner.icon,
            contentDescription = null,
            tint = accent,
            size = dims.sizing.iconMd
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

private const val AUTO_DISMISS_MS = 4000L
