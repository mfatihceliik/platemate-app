package com.mefy.platemate.data.local

/**
 * Bildirim izni isteminin durumu (kalıcı). Messages ekranına ilk girişte izin bir kez istenir;
 * sonraki ziyaretlerde tekrar sormamak için bayrak burada tutulur.
 */
interface NotificationPermissionStore {
    suspend fun hasRequestedOnMessages(): Boolean
    suspend fun setRequestedOnMessages()
}
