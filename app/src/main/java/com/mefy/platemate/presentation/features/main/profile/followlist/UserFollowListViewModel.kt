package com.mefy.platemate.presentation.features.main.profile.followlist

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.model.follow.FollowListItem
import com.mefy.platemate.domain.usecase.follow.FollowUserUseCase
import com.mefy.platemate.domain.usecase.follow.GetFollowersUseCase
import com.mefy.platemate.domain.usecase.follow.GetFollowingUseCase
import com.mefy.platemate.domain.usecase.follow.UnfollowUserUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.UserFollowListDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class UserFollowListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFollowingUseCase: GetFollowingUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private companion object {
        const val ERROR_CODE_FOLLOWING_HIDDEN = "follow.following.hidden"
    }

    private val route: UserFollowListDestination = savedStateHandle.toRoute()
    private val targetUserId: Long = route.userId.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(UserFollowListUiState(selectedTab = route.initialTab))
    val uiState: StateFlow<UserFollowListUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UserFollowListUiEffect>()
    val uiEffect: SharedFlow<UserFollowListUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: UserFollowListUiAction) {
        when (action) {
            is UserFollowListUiAction.TabChanged ->
                _uiState.update { it.copy(selectedTab = action.tabIndex) }

            is UserFollowListUiAction.UserClicked ->
                _uiEffect.emitUiEffect(UserFollowListUiEffect.NavigateToUserProfile(action.userId))

            is UserFollowListUiAction.FollowToggleClicked ->
                toggleFollow(action.userId, action.currentlyFollowing)

            UserFollowListUiAction.BackClicked ->
                _uiEffect.emitUiEffect(UserFollowListUiEffect.NavigateBack)
        }
    }

    override fun onRetry() {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, isFollowingListHidden = false) }
        launch(onError = ::handleError) {
            val followingDeferred = async { getFollowingUseCase(targetUserId) }
            val followersDeferred = async { getFollowersUseCase(targetUserId) }

            val followingResult = followingDeferred.await()
            val followersResult = followersDeferred.await()

            val followers = (followersResult as? AppResult.Success)?.data.orEmpty().map { it.toUiModel() }
            val followersError = (followersResult as? AppResult.Error)?.error

            when (followingResult) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            following = followingResult.data.map { item -> item.toUiModel() },
                            followers = followers,
                            isFollowingListHidden = false,
                            errorMessage = followersError?.toUiText()
                        )
                    }
                }
                is AppResult.Error -> {
                    val hidden = (followingResult.error as? AppError.Api)?.errorCode == ERROR_CODE_FOLLOWING_HIDDEN
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            following = emptyList(),
                            followers = followers,
                            isFollowingListHidden = hidden,
                            errorMessage = if (hidden) null else followingResult.error.toUiText()
                        )
                    }
                }
            }
        }
    }

    private fun toggleFollow(userId: Long, currentlyFollowing: Boolean) {
        updateIsFollowing(userId, !currentlyFollowing)
        launch(onError = {
            updateIsFollowing(userId, currentlyFollowing)
            handleError(it)
        }) {
            val result = if (currentlyFollowing) unfollowUserUseCase(userId) else followUserUseCase(userId)
            if (result is AppResult.Error) {
                updateIsFollowing(userId, currentlyFollowing)
                showError(result.error.toUiText())
            }
        }
    }

    private fun updateIsFollowing(userId: Long, isFollowing: Boolean) {
        _uiState.update { state ->
            state.copy(
                following = state.following.map { if (it.userId == userId) it.copy(isFollowing = isFollowing) else it },
                followers = state.followers.map { if (it.userId == userId) it.copy(isFollowing = isFollowing) else it }
            )
        }
    }

    private fun FollowListItem.toUiModel(): FollowListItemUiModel {
        val display = displayName?.takeIf { it.isNotBlank() } ?: username
        return FollowListItemUiModel(
            userId = id,
            displayName = display,
            username = "@$username",
            bio = bio.orEmpty(),
            isFollowing = isFollowing
        )
    }
}
