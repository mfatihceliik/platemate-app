package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.social.SocialPlatform
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.domain.usecase.social.GetPendingFriendRequestsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiMapper
import com.mefy.platemate.presentation.features.main.profile.reducer.ProfileStateReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
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
    private val getProfileUseCase: GetProfileUseCase,
    private val getSocialPlatformsUseCase: GetSocialPlatformsUseCase,
    private val getPendingFriendRequestsUseCase: GetPendingFriendRequestsUseCase,
    private val profileUiMapper: ProfileUiMapper,
    private val profileStateReducer: ProfileStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileUiEffect>()
    val uiEffect: SharedFlow<ProfileUiEffect> = _uiEffect.asSharedFlow()

    private var currentUserId: Long? = null

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
            ProfileUiAction.OnResume -> loadProfile(mode = LoadMode.SILENT)
            ProfileUiAction.RefreshRequested -> onRefreshRequested()
            ProfileUiAction.RetryClicked -> loadProfile(mode = LoadMode.INITIAL)
        }
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
        when (mode) {
            LoadMode.INITIAL -> _uiState.update(profileStateReducer::onInitialLoading)
            LoadMode.REFRESH -> _uiState.update(profileStateReducer::onRefreshing)
            LoadMode.SILENT -> Unit
        }
        launch(
            onError = { throwable ->
                applyLoadError(mode, UiText.Resource(R.string.common_error_unknown))
                handleError(throwable)
            }
        ) {
            val platformsDeferred = async { getSocialPlatformsUseCase() }
            val profileDeferred = async { getProfileUseCase(userId = userId) }
            val pendingRequestsDeferred = async { getPendingFriendRequestsUseCase() }

            val platforms: List<SocialPlatform> = when (val platformsResult = platformsDeferred.await()) {
                is AppResult.Success -> platformsResult.data
                is AppResult.Error -> emptyList()
            }
            // Badge count is a lenient enhancement: a failure here must not fail the whole profile load.
            val pendingFriendRequestCount = when (val pendingResult = pendingRequestsDeferred.await()) {
                is AppResult.Success -> pendingResult.data.size
                is AppResult.Error -> 0
            }
            when (val result = profileDeferred.await()) {
                is AppResult.Success -> {
                    val mapped = profileUiMapper.mapProfile(result.data, platforms)
                    _uiState.update { current ->
                        profileStateReducer.onProfileLoaded(current, mapped)
                            .copy(pendingFriendRequestCount = pendingFriendRequestCount)
                    }
                }

                is AppResult.Error -> applyLoadError(mode, result.error.toUiText())
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
}
