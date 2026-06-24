package com.mefy.platemate.core.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Cihazın internet/ağ bağlantısını gözlemler.
 *
 * Soyutlama (DIP) bilinçli: gerçek implementasyon Android `ConnectivityManager` kullanır,
 * testlerde sahte bir akış ile değiştirilebilir.
 */
interface NetworkMonitor {

    /** `true` = doğrulanmış internet erişimi var; `false` = yok. */
    val isOnline: Flow<Boolean>

    /**
     * Anlık (senkron) bağlantı durumu. Akış toplamadan, tek seferlik kontrol için
     * (ör. hata mesajı seçerken) kullanılır.
     */
    fun isCurrentlyOnline(): Boolean
}
