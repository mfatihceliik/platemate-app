package com.mefy.platemate.core.error

/**
 * Uygulama genelinde tek hata modeli. Yalnızca UX'in ayırt etmesi gereken
 * anlamlı durumlar vardır; her durum tek bir kullanıcı davranışına eşlenir.
 */
sealed class AppError(
    open val message: String? = null,
    open val cause: Throwable? = null
) {
    /** Cihaz çevrimdışı (internet yok). UX: snackbar. */
    data class Network(override val cause: Throwable? = null) : AppError(cause = cause)

    /**
     * Sunucuya ulaşılamadı: zaman aşımı, bağlantı reddi, 5xx ya da çözümlenemeyen
     * yanıt. UX: bloklayan pop-up.
     */
    data class Unreachable(override val cause: Throwable? = null) : AppError(cause = cause)

    /**
     * Backend isteği reddetti (`success=false` veya 4xx). Backend mesajını ve varsa
     * alan hatalarını taşır. UX: snackbar / inline.
     */
    data class Server(
        override val message: String?,
        val fieldErrors: Map<String, String>? = null,
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    /** 401/403 — oturum/token geçersiz. UX: yeniden giriş akışı. */
    object SessionExpired : AppError(message = "Session expired")
}
