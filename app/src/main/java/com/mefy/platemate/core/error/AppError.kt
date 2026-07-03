package com.mefy.platemate.core.error

/**
 * Uygulama genelinde tek hata modeli. Yalnızca UX'in ayırt etmesi gereken
 * anlamlı durumlar vardır; her durum tek bir kullanıcı davranışına eşlenir.
 */
sealed class AppError {

    /**
     * Ağ/bağlantı hatası: çevrimdışı, zaman aşımı, bağlantı reddi, 5xx
     * veya çözümlenemeyen yanıt. UX: bloklayan pop-up.
     *
     * [isOffline] `true` ise DNS çözülememiş — cihaz büyük olasılıkla çevrimdışı.
     */
    data class Network(
        val isOffline: Boolean = false,
        val cause: Throwable? = null
    ) : AppError()

    /**
     * Backend isteği reddetti (`success=false` veya 4xx). Backend mesajını ve varsa
     * alan hatalarını taşır. UX: snackbar / inline.
     */
    data class Api(
        val message: String?,
        val errorCode: String? = null,
        val fieldErrors: Map<String, String>? = null
    ) : AppError()

    /** 401 — oturum/token geçersiz. UX: yeniden giriş akışı. */
    data object SessionExpired : AppError()
}
