package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
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
    private val getProfileUseCase: GetProfileUseCase,
    private val profileUiMapper: ProfileUiMapper,
    private val profileStateReducer: ProfileStateReducer,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

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
            is ProfileUiAction.PlateReviewClicked -> onPlateReviewClicked(action.normalizedPlateCode)
            ProfileUiAction.SettingsClicked -> _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToSettings)
            ProfileUiAction.FriendsClicked -> _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToFriends)
            ProfileUiAction.OnResume -> loadProfile(isSilentRefresh = true)
        }
    }

    private fun observeSession(observeSessionUseCase: ObserveSessionUseCase) {
        launch(
            onError = { throwable ->
                _uiState.update(profileStateReducer::onLoadFailed)
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

    private fun onPlateReviewClicked(normalizedPlateCode: String) {
        _uiEffect.emitUiEffect(ProfileUiEffect.NavigateToSearchDetail(normalizedPlateCode))
    }

    private fun loadProfile(isSilentRefresh: Boolean = false) {
        val userId = currentUserId ?: return
        if (!isSilentRefresh) {
            _uiState.update(profileStateReducer::onInitialLoading)
        }
        launch(
            onError = { throwable ->
                if (!isSilentRefresh) {
                    _uiState.update(profileStateReducer::onLoadFailed)
                }
                handleError(throwable)
            }
        ) {
            when (val result = getProfileUseCase(userId = userId)) {
                is AppResult.Success -> {
                    val mapped = profileUiMapper.mapProfile(result.data)
                    _uiState.update { current ->
                        profileStateReducer.onProfileLoaded(current, mapped)
                    }
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    if (!isSilentRefresh) {
                        _uiState.update(profileStateReducer::onLoadFailed)
                    }
                }
            }
        }
    }
}
