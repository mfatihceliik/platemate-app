package com.mefy.platemate.presentation.features.main.profile.friends

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.social.GetFriendsUseCase
import com.mefy.platemate.domain.usecase.social.AcceptFriendRequestUseCase
import com.mefy.platemate.domain.usecase.social.RejectFriendRequestUseCase
import com.mefy.platemate.domain.usecase.social.GetPendingFriendRequestsUseCase
import com.mefy.platemate.domain.usecase.social.GetSentFriendRequestsUseCase
import com.mefy.platemate.domain.usecase.social.RemoveFriendUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.ProfileFriendsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileFriendsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getPendingFriendRequestsUseCase: GetPendingFriendRequestsUseCase,
    private val getSentFriendRequestsUseCase: GetSentFriendRequestsUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val rejectFriendRequestUseCase: RejectFriendRequestUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ProfileFriendsDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(ProfileFriendsUiState(selectedTab = route.initialTab))
    val uiState: StateFlow<ProfileFriendsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileFriendsUiEffect>()
    val uiEffect: SharedFlow<ProfileFriendsUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: ProfileFriendsUiAction) {
        when (action) {
            is ProfileFriendsUiAction.SearchQueryChanged -> {
                val query = action.query
                _uiState.update { state ->
                    state.copy(
                        searchQuery = query,
                        filteredFriends = filterFriends(state.friends, query),
                        filteredPendingRequests = filterFriends(state.pendingRequests, query),
                        filteredSentRequests = filterFriends(state.sentRequests, query)
                    )
                }
            }
            is ProfileFriendsUiAction.TabChanged -> {
                _uiState.update { it.copy(selectedTab = action.tabIndex) }
            }
            is ProfileFriendsUiAction.AcceptRequestClicked -> acceptRequest(action.friendshipId)
            is ProfileFriendsUiAction.RejectRequestClicked -> rejectRequest(action.friendshipId)
            is ProfileFriendsUiAction.RemoveFriendClicked -> {
                _uiState.update {
                    it.copy(
                        showRemoveFriendPopup = true,
                        selectedFriendIdForRemoval = action.friendshipId,
                        isCancelingSentRequest = false
                    )
                }
            }
            is ProfileFriendsUiAction.CancelRequestClicked -> {
                _uiState.update {
                    it.copy(
                        showRemoveFriendPopup = true,
                        selectedFriendIdForRemoval = action.friendshipId,
                        isCancelingSentRequest = true
                    )
                }
            }
            ProfileFriendsUiAction.CancelRemoveFriend -> {
                _uiState.update {
                    it.copy(showRemoveFriendPopup = false, selectedFriendIdForRemoval = null, isCancelingSentRequest = false)
                }
            }
            ProfileFriendsUiAction.ConfirmRemoveFriend -> {
                val friendId = _uiState.value.selectedFriendIdForRemoval
                if (friendId != null) {
                    removeFriend(friendId)
                }
                _uiState.update {
                    it.copy(showRemoveFriendPopup = false, selectedFriendIdForRemoval = null, isCancelingSentRequest = false)
                }
            }
            is ProfileFriendsUiAction.FriendClicked -> {
                sendEffect(ProfileFriendsUiEffect.NavigateToUserProfile(action.friendUserId))
            }
            ProfileFriendsUiAction.BackClicked -> {
                sendEffect(ProfileFriendsUiEffect.NavigateBack)
            }
            ProfileFriendsUiAction.RetryClicked -> {
                load()
            }
        }
    }

    private fun filterFriends(friends: List<ProfileFriendUiModel>, query: String): List<ProfileFriendUiModel> {
        if (query.isBlank()) return friends
        val lowerQuery = query.lowercase()
        return friends.filter { it.username.lowercase().contains(lowerQuery) }
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = ::handleError) {
            val friendsDeferred = async { getFriendsUseCase() }
            val pendingDeferred = async { getPendingFriendRequestsUseCase() }
            val sentDeferred = async { getSentFriendRequestsUseCase() }

            val friendsResult = friendsDeferred.await()
            val pendingResult = pendingDeferred.await()
            val sentResult = sentDeferred.await()

            if (friendsResult is AppResult.Success && pendingResult is AppResult.Success && sentResult is AppResult.Success) {
                val friendsList = friendsResult.data.map { friend ->
                    val dateStr = friend.respondedAt?.iso8601?.take(10).orEmpty()
                    ProfileFriendUiModel(
                        id = friend.id,
                        friendUserId = friend.friendUserId,
                        username = friend.friendUsername,
                        status = friend.status.name,
                        subtitleText = if (dateStr.isNotBlank()) "Arkadaşlık tarihi: $dateStr" else ""
                    )
                }
                val pendingList = pendingResult.data.map { friend ->
                    val dateStr = friend.requestedAt?.iso8601?.take(10).orEmpty()
                    ProfileFriendUiModel(
                        id = friend.id,
                        friendUserId = friend.friendUserId,
                        username = friend.friendUsername, // In pending, requester is the friend username if I am addressee
                        status = friend.status.name,
                        subtitleText = if (dateStr.isNotBlank()) "İstek tarihi: $dateStr" else ""
                    )
                }
                val sentList = sentResult.data.map { friend ->
                    val dateStr = friend.requestedAt?.iso8601?.take(10).orEmpty()
                    ProfileFriendUiModel(
                        id = friend.id,
                        friendUserId = friend.friendUserId,
                        username = friend.friendUsername, // In sent, friendUsername is the addressee I sent the request to
                        status = friend.status.name,
                        subtitleText = if (dateStr.isNotBlank()) "İstek tarihi: $dateStr" else ""
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        friends = friendsList,
                        filteredFriends = filterFriends(friendsList, it.searchQuery),
                        pendingRequests = pendingList,
                        filteredPendingRequests = filterFriends(pendingList, it.searchQuery),
                        sentRequests = sentList,
                        filteredSentRequests = filterFriends(sentList, it.searchQuery)
                    )
                }
            } else {
                val error = (friendsResult as? AppResult.Error)
                    ?: (pendingResult as? AppResult.Error)
                    ?: (sentResult as? AppResult.Error)
                _uiState.update { it.copy(isLoading = false, errorMessage = error?.error?.toUiText()) }
            }
        }
    }

    private fun acceptRequest(friendshipId: Long) {
        launch(onError = ::handleError) {
            val result = acceptFriendRequestUseCase(friendshipId)
            if (result is AppResult.Success) {
                load()
            }
        }
    }

    private fun rejectRequest(friendshipId: Long) {
        launch(onError = ::handleError) {
            val result = rejectFriendRequestUseCase(friendshipId)
            if (result is AppResult.Success) {
                load()
            }
        }
    }

    private fun removeFriend(friendUserId: Long) {
        launch(onError = ::handleError) {
            val result = removeFriendUseCase(friendUserId)
            if (result is AppResult.Success) {
                load()
            }
        }
    }

    private fun sendEffect(effect: ProfileFriendsUiEffect) {
        launch { _uiEffect.emit(effect) }
    }
}
