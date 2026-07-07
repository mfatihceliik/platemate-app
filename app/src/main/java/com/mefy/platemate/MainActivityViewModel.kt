package com.mefy.platemate

import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.connectivity.NetworkMonitor
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.global.InAppNotificationBus
import com.mefy.platemate.presentation.common.global.NotificationNavigationBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Kök ekranın UI durumunu sağlar. App-geneli yan etkiler (socket lifecycle, FCM kaydı, canlı mesaj
 * cache) artık [com.mefy.platemate.core.startup.AppCoordinator] implementasyonlarında — bu VM
 * yalnızca kimlik/online/olay akışını tutar.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    networkMonitor: NetworkMonitor,
    globalUiEventBus: GlobalUiEventBus,
    inAppNotificationBus: InAppNotificationBus,
    private val notificationNavigationBus: NotificationNavigationBus
) : BaseViewModel() {

    /** Uygulama-geneli kritik olaylar; [AppNavHost] tek noktadan tüketir. */
    val globalUiEvents: SharedFlow<GlobalAppEvent> = globalUiEventBus.events

    /** App AÇIKKEN gelen bildirimler; kök composable banner olarak gösterir. */
    val inAppNotifications: SharedFlow<AppNotification> = inAppNotificationBus.events

    /** Sistem bildirimine dokunmadan gelen yönlendirme hedefi; kök composable tüketip nav eder. */
    val notificationNavTarget: StateFlow<AppNotification?> = notificationNavigationBus.target

    fun consumeNotificationNavTarget() = notificationNavigationBus.consume()

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
