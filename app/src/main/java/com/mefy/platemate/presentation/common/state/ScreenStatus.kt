package com.mefy.platemate.presentation.common.state

import com.mefy.platemate.presentation.common.text.UiText

/**
 * Bir ekranın birincil içerik durumunun genel modeli. [com.mefy.platemate.presentation.components.PMBaseScreen]
 * bunu tek bir Crossfade ile çizer: yükleme / içerik / boş / hata(mesaj + tekrar dene).
 *
 * Tek sealed değer üzerinden Crossfade yapılması titremeyi (flicker) de önler: animasyon
 * sırasında çıkan kare kendi durumunu (ör. [Error]) korur, [Content]'e yeniden çözülmez.
 */
sealed interface ScreenStatus {
    data object Loading : ScreenStatus
    data object Content : ScreenStatus
    data object Empty : ScreenStatus
    data class Error(val message: UiText) : ScreenStatus
}
