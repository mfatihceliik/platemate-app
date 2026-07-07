package com.mefy.platemate.core.util

import android.util.Base64
import org.json.JSONObject

/**
 * Access token'ın (JWT) süresinin dolup dolmadığını ağ olmadan, istemci tarafında kontrol eder.
 * Socket el sıkışması süresi dolmuş token'ı reddettiği için (401 → "xhr poll error"), bağlanmadan
 * önce yenileme gerekip gerekmediğini buradan anlarız.
 *
 * Çözülemeyen ya da `exp` içermeyen token'da güvenli taraf seçilir: süresi dolmuş kabul edilir
 * (yenilemeye zorlar) — geçersiz bir token ile el sıkışmaya çalışmaktansa.
 */
fun String.isAccessTokenExpired(leewaySeconds: Long = 60): Boolean {
    val exp = jwtExpirySeconds() ?: return true
    val nowSeconds = System.currentTimeMillis() / 1000
    return nowSeconds + leewaySeconds >= exp
}

private fun String.jwtExpirySeconds(): Long? = runCatching {
    val parts = split(".")
    if (parts.size < 2) return null
    val payloadJson = String(
        Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    )
    JSONObject(payloadJson).optLong("exp", 0L).takeIf { it > 0L }
}.getOrNull()
