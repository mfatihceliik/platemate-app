package com.mefy.platemate.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.topbar.PMTopBar
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.theme.pmColors

/**
 * Tüm ekranların ortak iskeleti: üst bar + içerik + alt bar.
 *
 * [status] verildiğinde içerik alanı tek bir Crossfade ile yüklenme / içerik / boş /
 * hata(mesaj + tekrar dene) durumlarını çizer; böylece her veri ekranı aynı davranışı
 * tekrar yazmadan kazanır ve durum geçişlerinde titreme (flicker) olmaz.
 * [status] = null olduğunda yalnızca [content] çizilir (geriye-dönük uyumlu; auth/form
 * ekranları için).
 */
@Composable
fun PMBaseScreen(
    modifier: Modifier = Modifier,
    topBarConfig: PMTopBarConfig = PMTopBarConfig.Hidden,
    containerColor: Color = MaterialTheme.pmColors.background,
    topBarContainerColor: Color = containerColor,
    bottomBar: @Composable () -> Unit = {},
    status: ScreenStatus? = null,
    onRetry: () -> Unit = {},
    loading: @Composable (PaddingValues) -> Unit = {},
    empty: @Composable (PaddingValues) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldPadding = LocalScaffoldPadding.current
    val isOnline = LocalIsOnline.current

    // Çevrimdışıyken tüm veri ekranları (status != null) tam-ekran bağlantı hatasına
    // geçer; bağlantı dönünce gerçek duruma otomatik çözülür. Form/auth ekranları
    // (status == null) etkilenmez.
    val effectiveStatus = if (status != null && !isOnline) {
        ScreenStatus.Error(UiText.Resource(R.string.common_error_network))
    } else {
        status
    }

    // Yükleme (shimmer) ve hata durumunda topbar + ekran-içi alt bar gizlenir; iskelet/
    // hata tam ekran görünür. Sekme nav barı AppNavHost'tan gelir, etkilenmez.
    val hideChrome = effectiveStatus == ScreenStatus.Loading || effectiveStatus is ScreenStatus.Error

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor)
    ) {
        if (!hideChrome) {
            PMTopBar(config = topBarConfig, containerColor = topBarContainerColor)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .then(
                    // Topbar gizliyken (Hidden config ya da yükleme) status-bar boşluğunu içerik üstlenir.
                    if (hideChrome || topBarConfig is PMTopBarConfig.Hidden)
                        Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    else Modifier
                )
        ) {
            if (effectiveStatus == null) {
                content(scaffoldPadding)
            } else {
                Crossfade(targetState = effectiveStatus, label = "screen_status") { current ->
                    when (current) {
                        ScreenStatus.Loading -> loading(scaffoldPadding)
                        ScreenStatus.Empty -> empty(scaffoldPadding)
                        ScreenStatus.Content -> content(scaffoldPadding)
                        is ScreenStatus.Error -> PMErrorState(
                            message = current.message,
                            onRetry = onRetry,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        if (!hideChrome) {
            bottomBar()
        }
    }
}
