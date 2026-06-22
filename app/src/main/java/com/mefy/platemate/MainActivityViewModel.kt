package com.mefy.platemate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.connectivity.NetworkMonitor
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    networkMonitor: NetworkMonitor,
    globalUiEventBus: GlobalUiEventBus
) : ViewModel() {

    /** Uygulama-geneli kritik olaylar; [AppNavHost] tek noktadan tüketir. */
    val globalUiEvents: SharedFlow<GlobalAppEvent> = globalUiEventBus.events

    val isAuthenticated: StateFlow<Boolean?> = observeSessionUseCase()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null // null means loading/unknown state initially
        )

    // Başlangıçta `true` varsayılır; gerçek durum gelene kadar yanlış "çevrimdışı" göstermeyiz.
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
}
