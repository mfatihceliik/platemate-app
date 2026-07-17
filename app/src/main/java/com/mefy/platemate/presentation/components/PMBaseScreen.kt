package com.mefy.platemate.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.state.ScreenStatus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.topbar.PMTopBar
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.util.plus
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Tüm ekranların ortak iskeleti: üst bar + içerik + alt bar.
 *
 * [status] verildiğinde içerik alanı tek bir Crossfade ile yüklenme / içerik / boş /
 * hata(mesaj + tekrar dene) durumlarını çizer; böylece her veri ekranı aynı davranışı
 * tekrar yazmadan kazanır ve durum geçişlerinde titreme (flicker) olmaz.
 * [status] = null olduğunda yalnızca [content] çizilir (geriye-dönük uyumlu; auth/form
 * ekranları için).
 *
 * Padding sözleşmesi: [content]/[loading]/[empty]'e geçen [PaddingValues] kozmetik bir
 * kenar boşluğu DEĞİL; floating alt-bar + sistem inset boşluğudur ([LocalScaffoldPadding]).
 * Ekran bunu kendi tüketim biçimine göre uygular: liste ekranları `contentPadding` olarak
 * (içerik hap-barın altından kayar, zemin sürekli kalır), statik/scroll ekranlar `.padding`
 * olarak. Kendi yatay/dikey gutter'ını (s12/s16 vb.) ekran ayrıca ekler. Bu yüzden base
 * sabit bir tüm-köşe padding'i dayatmaz — full-bleed listeleri ve per-screen gutter'ları korur.
 *
 * [contentPadding] = çağıranın opt-in kozmetik gutter'ı; dondurulmuş scaffold inset'i ile kenar-
 * kenar birleştirilip [content]/[loading]/[empty]'e TEK hazır [PaddingValues] olarak verilir.
 * Böylece ekran `s16 + innerPadding.calculateBottomPadding()` aritmetiğini yazmaz. Default 0.dp →
 * merge no-op → bugünkü full-bleed davranış birebir korunur (geriye-dönük uyumlu).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBaseScreen(
    modifier: Modifier = Modifier,
    topBarConfig: PMTopBarConfig = PMTopBarConfig.Hidden,
    containerColor: Color = MaterialTheme.pmColors.background,
    topBarContainerColor: Color = MaterialTheme.pmColors.background,
    applyImePadding: Boolean = false,
    bottomBar: @Composable () -> Unit = {},
    // İçerik alanının ALTINA hizalı, içeriğin/empty'nin ÜZERİNE binen kalıcı katman
    // (ör. sohbet composer'ı). bottomBar'dan farkı: content'i aşağı itmez, üstüne çizer;
    // böylece durum (Content/Empty) değişse de kalır ve arkasındaki içerik görünür.
    // BoxScope alıcısıdır → çağıran `Modifier.align(...)` kullanabilir. Default {} → diğer
    // ekranlar etkilenmez.
    contentBottomOverlay: @Composable BoxScope.() -> Unit = {},
    status: ScreenStatus? = null,
    onRetry: () -> Unit = {},
    // Opt-in kozmetik gutter; scaffold inset'i ile merge edilip content/loading/empty'e verilir.
    // Default 0 → no-op (full-bleed korunur). Bkz. üstteki padding sözleşmesi.
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.pmDimensions.spacing.s8),
    keepTopBarWhileLoading: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    loading: @Composable (PaddingValues) -> Unit = {},
    empty: @Composable (PaddingValues) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    // Padding giriş anında dondurulur: alt bar görünürlüğü destinasyonun saf fonksiyonu
    // (NavChromeRulesTest) ve bu ekranın destinasyonu ömrü boyunca değişmez. Canlı okunursa
    // navigasyon anında (çıkış animasyonu bitmeden) padding 108dp→0 düşer ve çıkan ekranın
    // içeriği aşağı kayar. Giren ekran backstack güncellendikten sonra compose olduğu için
    // her zaman doğru güncel değeri yakalar.
    val liveScaffoldPadding = LocalScaffoldPadding.current
    val scaffoldPadding = remember { liveScaffoldPadding }
    val isOnline = LocalIsOnline.current

    // Çağıranın opt-in gutter'ı (contentPadding), dondurulmuş inset ile kenar-kenar birleşir;
    // content/loading/empty bu tek değeri tüketir. Default 0.dp → merge no-op (full-bleed korunur).
    val layoutDirection = LocalLayoutDirection.current
    val mergedPadding = remember(scaffoldPadding, contentPadding, layoutDirection) {
        scaffoldPadding.plus(contentPadding, layoutDirection)
    }

    // [onRefresh] verildiğinde içerik aşağı-çekerek-yenile ile sarılır. Indicator, içerik
    // alanının (topbar'ın ALTINDA) üst-ortasına küçük boşlukla yerleşir; böylece dikişten
    // çıkar gibi görünmez. Yalnızca gerçek içerik sarılır (loading/error/empty değil).
    val renderContent: @Composable (PaddingValues) -> Unit = { padding ->
        if (onRefresh != null) {
            PMPullToRefresh(isRefreshing = isRefreshing, onRefresh = onRefresh) {
                content(padding)
            }
        } else {
            content(padding)
        }
    }

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
    // [keepTopBarWhileLoading] = true iken yüklemede topbar görünür kalır (yalnızca hata
    // chrome'u gizler); böylece yükleme→içerik geçişinde topbar zıplaması/boş ekran hissi olmaz.
    val hideChrome = (effectiveStatus == ScreenStatus.Loading && !keepTopBarWhileLoading) ||
            effectiveStatus is ScreenStatus.Error

    val scrollBehavior = if (topBarConfig is PMTopBarConfig.Transparent) TopAppBarDefaults.enterAlwaysScrollBehavior() else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor)
            .then(
                // Klavyeli ekranlar (ör. konuşma) opt-in eder: kök, (ime − navbar) kadar kalkar;
                // alt bar kendi navbar boşluğunu ekler → toplam tam ime yüksekliği. Kapalıyken 0.
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
                    // Topbar gizliyken (Hidden config ya da yükleme) status-bar boşluğunu içerik üstlenir.
                    if (hideChrome || topBarConfig is PMTopBarConfig.Hidden)
                        Modifier.windowInsetsPadding(WindowInsets.statusBars)
                    else Modifier
                )
        ) {
            if (effectiveStatus == null) {
                renderContent(mergedPadding)
            } else {
                Crossfade(targetState = effectiveStatus, label = "screen_status") { current ->
                    when (current) {
                        ScreenStatus.Loading -> loading(mergedPadding)
                        ScreenStatus.Empty -> empty(mergedPadding)
                        ScreenStatus.Content -> renderContent(mergedPadding)
                        is ScreenStatus.Error -> PMErrorState(
                            message = current.message,
                            onRetry = onRetry,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            // Content ve Empty durumlarının ÜZERİNE binen kalıcı alt katman; yükleme/hata
            // chrome'unda (hideChrome) gizli — o durumlarda tam-ekran iskelet/hata görünür.
            if (!hideChrome) {
                contentBottomOverlay()
            }
            
            if (!hideChrome && topBarConfig is PMTopBarConfig.Transparent) {
                PMTopBar(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter),
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