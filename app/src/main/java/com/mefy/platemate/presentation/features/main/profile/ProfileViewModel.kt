package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfilePageUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiMapper
import com.mefy.platemate.presentation.features.main.profile.reducer.ProfileStateReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
    private val getProfilePageUseCase: GetProfilePageUseCase,
    private val profileUiMapper: ProfileUiMapper,
    private val profileStateReducer: ProfileStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileUiEffect>()
    val uiEffect: SharedFlow<ProfileUiEffect> = _uiEffect.asSharedFlow()

    private var currentUserId: Long? = null

    // ProfileRoute'un ON_RESUME senkron replay'i (init'teki ilk yüklemeyle aynı anda) ve olası
    // başka çakışan tetikleyiciler için: aynı anda tek yükleme.
    private var isLoadInFlight = false

    // Bottom bar'da başka bir tab'a gidip Profile'a her dönüşte ON_RESUME tekrar tetiklenir
    // (NavBackStackEntry saveState/restoreState ile yeniden yaratılıyor) — bu, art arda hızlı tab
    // geçişlerinde gereksiz ağ isteğine yol açar. ChatRepositoryImpl.syncChatRooms()'daki
    // TTL-debounce ile aynı fikir: son başarılı yüklemeden beri yeterli süre geçmediyse
    // ON_RESUME'u sessizce yok say; RefreshRequested/RetryClicked bundan etkilenmez.
    private var lastLoadedAtMs = 0L

    init {
        observeSession(observeSessionUseCase)
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.PlateReviewClicked -> onPlateReviewClicked(action.normalizedPlateCode, action.reviewId)
            ProfileUiAction.FriendsClicked -> _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToFriends(initialTab = 0))
            ProfileUiAction.FriendRequestActivityClicked -> _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToFriends(initialTab = 1))
            is ProfileUiAction.StatusSummaryClicked -> _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToReviewList(action.status))
            is ProfileUiAction.ActivityTabChanged -> _uiState.update { it.copy(selectedActivityTab = action.tabIndex) }
            ProfileUiAction.OnResume -> onResume()
        }
    }

    override fun onRetry() {
        loadProfile(mode = LoadMode.INITIAL)
    }

    override fun onRefresh() {
        onRefreshRequested()
    }
    
    private fun onResume() {
        val elapsed = System.currentTimeMillis() - lastLoadedAtMs
        if (elapsed < PROFILE_RESUME_RELOAD_MIN_INTERVAL_MS) return
        loadProfile(mode = LoadMode.SILENT)
    }

    private fun onRefreshRequested() {
        val state = _uiState.value
        if (state.isInitialLoading || state.isRefreshing) return
        loadProfile(mode = LoadMode.REFRESH)
    }
    private fun observeSession(observeSessionUseCase: ObserveSessionUseCase) {
        launch(
            onError = { throwable ->
                applyLoadError(LoadMode.INITIAL, UiText.Resource(R.string.common_error_unknown))
                handleError(throwable)
            }
        ) {
            observeSessionUseCase().collectLatest { session ->
                val userId = session?.userId ?: return@collectLatest
                val shouldLoad = currentUserId != userId || _uiState.value.isInitialLoading
                currentUserId = userId
                if (shouldLoad) {
                    loadProfile()
                }
            }
        }
    }

    private fun onPlateReviewClicked(normalizedPlateCode: String, reviewId: Long) {
        _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToReviewDetail(normalizedPlateCode, reviewId))
    }

    private fun loadProfile(mode: LoadMode = LoadMode.INITIAL) {
        val userId = currentUserId ?: return
        if (isLoadInFlight) return
        isLoadInFlight = true
        when (mode) {
            LoadMode.INITIAL -> _uiState.update(profileStateReducer::onInitialLoading)
            LoadMode.REFRESH -> _uiState.update(profileStateReducer::onRefreshing)
            LoadMode.SILENT -> Unit
        }
        launch(
            onError = { throwable ->
                isLoadInFlight = false
                applyLoadError(mode, UiText.Resource(R.string.common_error_unknown))
                handleError(throwable)
            }
        ) {
            try {
                when (val result = getProfilePageUseCase(userId)) {
                    is AppResult.Success -> {
                        val page = result.data
                        val mapped = profileUiMapper.mapProfile(page.profile, page.socialPlatforms)
                        _uiState.update { current ->
                            profileStateReducer.onProfileLoaded(current, mapped)
                                .copy(pendingFriendRequestCount = page.pendingFriendRequests.size)
                        }
                        lastLoadedAtMs = System.currentTimeMillis()
                    }

                    is AppResult.Error -> applyLoadError(mode, result.error.toUiText())
                }
            } finally {
                isLoadInFlight = false
            }
        }
    }

    /**
     * İlk yükleme hatası ekran-içi gösterilir (inline); yenileme/sessiz tazeleme hatası
     * içeriği silmemek için snackbar ile bildirilir.
     */
    private fun applyLoadError(mode: LoadMode, message: UiText) {
        when (mode) {
            LoadMode.INITIAL -> _uiState.update { profileStateReducer.onLoadFailed(it, message) }
            LoadMode.REFRESH -> {
                _uiState.update(profileStateReducer::onRefreshFailed)
                showError(message)
            }
            LoadMode.SILENT -> showError(message)
        }
    }

    private enum class LoadMode { INITIAL, REFRESH, SILENT }

    private companion object {
        const val PROFILE_RESUME_RELOAD_MIN_INTERVAL_MS = 30_000L
    }
}
