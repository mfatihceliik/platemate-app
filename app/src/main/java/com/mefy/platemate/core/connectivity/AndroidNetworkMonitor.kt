package com.mefy.platemate.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow

/**
 * [NetworkMonitor]'ün `ConnectivityManager` tabanlı implementasyonu.
 *
 * `registerNetworkCallback` ile ağ değişimlerini dinler ve doğrulanmış internet
 * ([NetworkCapabilities.NET_CAPABILITY_VALIDATED]) durumunu bir [Flow] olarak yayar.
 * Akış soğuk (cold): yalnızca abone varken callback kaydı tutulur, abone bitince
 * [awaitClose] ile temizlenir (kaynak sızıntısı yok).
 */
@Singleton
class AndroidNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkMonitor {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager == null) {
            trySend(false)
            channel.close()
            return@callbackFlow
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            private val onlineNetworks = mutableSetOf<Network>()

            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                val validated = capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) && capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                if (validated) onlineNetworks.add(network) else onlineNetworks.remove(network)
                trySend(onlineNetworks.isNotEmpty())
            }

            override fun onLost(network: Network) {
                onlineNetworks.remove(network)
                trySend(onlineNetworks.isNotEmpty())
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Başlangıç durumu: mevcut aktif ağ doğrulanmış mı?
        trySend(connectivityManager.isCurrentlyOnline())
        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
        .distinctUntilChanged()
        .conflate()
        .flowOn(Dispatchers.IO)

    override fun isCurrentlyOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                ?: return false
        return connectivityManager.isCurrentlyOnline()
    }

    private fun ConnectivityManager.isCurrentlyOnline(): Boolean {
        val network = activeNetwork ?: return false
        val capabilities = getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
