package com.mefy.platemate.presentation.common.banner

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Üstten inen banner'ın tek tip gösterim modeli. Hem push bildirimleri (tip ikonu + nav) hem
 * UI mesajları (hata/başarı/info) buna map'lenir. Renk [severity]'den banner'da çözülür; [icon]
 * sabit (composition dışı) olduğu için modelde taşınabilir. [onClick] varsa tıklanabilir.
 */
@Immutable
data class InAppBannerUiModel(
    val title: String?,
    val message: String,
    val icon: ImageVector,
    val severity: BannerSeverity,
    val onClick: (() -> Unit)? = null
)
