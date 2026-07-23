package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.local.RecentUserSearchStore
import com.mefy.platemate.domain.model.profile.ProfileFriendshipStatus
import com.mefy.platemate.domain.repository.SocialRepository
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.report.ReportUserUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.mapper.UserProfileMappingInput
import com.mefy.platemate.presentation.features.main.profile.userprofile.mapper.UserProfileUiMapper
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.features.uimodel.toUiModel
import com.mefy.platemate.presentation.navigation.UserProfileDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProfileUseCase: GetProfileUseCase,
    private val getSocialPlatformsUseCase: GetSocialPlatformsUseCase,
    private val socialRepository: SocialRepository,
    private val reportUserUseCase: ReportUserUseCase,
    private val recentUserSearchStore: RecentUserSearchStore,
    private val userProfileUiMapper: UserProfileUiMapper,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: UserProfileDestination = savedStateHandle.toRoute()
    private val userId: Long = route.userId.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(
        UserProfileUiState(userId = route.userId)
    )
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UserProfileUiEffect>()
    val uiEffect: SharedFlow<UserProfileUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadProfile()
    }

    fun onAction(action: UserProfileUiAction) {
        when (action) {
            UserProfileUiAction.BackClicked ->
                _uiEffect.emitUiEffect(UserProfileUiEffect.NavigateBack)

            UserProfileUiAction.AddFriendClicked -> onAddFriendClicked()

            UserProfileUiAction.CancelRequestClicked -> onRemoveFriendClicked()

            UserProfileUiAction.AcceptRequestClicked -> onAcceptRequestClicked()

            UserProfileUiAction.RemoveFriendClicked ->
                _uiState.update { it.copy(showRemoveFriendPopup = true) }

            UserProfileUiAction.RemoveFriendConfirmed -> onRemoveFriendClicked()

            UserProfileUiAction.RemoveFriendDismissed ->
                _uiState.update { it.copy(showRemoveFriendPopup = false) }

            UserProfileUiAction.MessageClicked -> onMessageClicked()

            UserProfileUiAction.ShareClicked -> Unit

            UserProfileUiAction.FollowersClicked ->
                _uiEffect.emitUiEffect(UserProfileUiEffect.NavigateToFollowList(route.userId, 0))

            UserProfileUiAction.FollowingClicked ->
                _uiEffect.emitUiEffect(UserProfileUiEffect.NavigateToFollowList(route.userId, 1))

            UserProfileUiAction.ReportMenuClicked ->
                _uiState.update { it.copy(showReportSheet = true) }

            is UserProfileUiAction.ReportReasonSelected ->
                _uiState.update { it.copy(selectedReportReason = action.reason) }

            is UserProfileUiAction.ReportOtherReasonTextChanged ->
                _uiState.update { it.copy(otherReportReasonText = action.text) }

            UserProfileUiAction.ReportSubmitClicked -> submitReport()

            UserProfileUiAction.ReportDismissed ->
                _uiState.update { it.copy(showReportSheet = false, selectedReportReason = null, otherReportReasonText = "") }
        }
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }
        launch {
            val platforms = when (val platformsResult = getSocialPlatformsUseCase()) {
                is AppResult.Success -> platformsResult.data.map { it.toUiModel() }
                is AppResult.Error -> emptyList()
            }
            when (val result = getProfileUseCase(userId)) {
                is AppResult.Success -> {
                    val uiData = userProfileUiMapper.map(UserProfileMappingInput(result.data, platforms))
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            displayName = uiData.displayName,
                            username = uiData.username,
                            bio = uiData.bio,
                            isVerified = uiData.isVerified,
                            isOnline = uiData.isOnline,
                            friendshipStatus = uiData.friendshipStatus,
                            friendshipId = uiData.friendshipId,
                            reviewCount = uiData.reviewCount,
                            followerCount = uiData.followerCount,
                            followingCount = uiData.followingCount,
                            socialLinks = uiData.socialLinks,
                            approvedReviews = uiData.approvedReviews
                        )
                    }
                    recentUserSearchStore.updateCachedProfile(userId, result.data.displayName, result.data.bio)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun onMessageClicked() {
        val state = _uiState.value
        _uiEffect.emitUiEffect(
            UserProfileUiEffect.NavigateToChat(
                conversationId = "",
                otherUserId = userId,
                participantName = state.displayName,
            )
        )
    }

    private fun onAddFriendClicked() {
        val prevState = _uiState.value.friendshipStatus
        _uiState.update { it.copy(friendshipStatus = ProfileFriendshipStatus.PENDING_SENT) }
        launch(onError = {
            _uiState.update { state -> state.copy(friendshipStatus = prevState) }
            handleError(it)
        }) {
            val result = socialRepository.sendFriendRequest(userId)
            if (result is AppResult.Error) {
                _uiState.update { it.copy(friendshipStatus = prevState) }
                showError(result.error.toUiText())
            } else {
                // Reload profile to get the new friendshipId
                loadProfile()
            }
        }
    }

    private fun onAcceptRequestClicked() {
        val friendshipId = _uiState.value.friendshipId ?: return
        val prevState = _uiState.value.friendshipStatus
        _uiState.update { it.copy(friendshipStatus = ProfileFriendshipStatus.FRIENDS) }
        launch(onError = {
            _uiState.update { state -> state.copy(friendshipStatus = prevState) }
            handleError(it)
        }) {
            val result = socialRepository.acceptFriendRequest(friendshipId)
            if (result is AppResult.Error) {
                _uiState.update { it.copy(friendshipStatus = prevState) }
                showError(result.error.toUiText())
            }
        }
    }

    private fun onRemoveFriendClicked() {
        val friendshipId = _uiState.value.friendshipId ?: return
        val prevState = _uiState.value.friendshipStatus
        _uiState.update { it.copy(friendshipStatus = ProfileFriendshipStatus.NONE, showRemoveFriendPopup = false) }
        launch(onError = {
            _uiState.update { state -> state.copy(friendshipStatus = prevState) }
            handleError(it)
        }) {
            val result = socialRepository.removeFriend(friendshipId)
            if (result is AppResult.Error) {
                _uiState.update { it.copy(friendshipStatus = prevState) }
                showError(result.error.toUiText())
            } else {
                _uiState.update { it.copy(friendshipId = null) }
            }
        }
    }

    private fun submitReport() {
        val reason = _uiState.value.selectedReportReason ?: return
        val otherText = _uiState.value.otherReportReasonText
        val reasonString = if (reason == ReportReason.OTHER && otherText.isNotBlank()) {
            "${reason.name}: $otherText"
        } else {
            reason.name
        }

        _uiState.update { it.copy(isSubmittingReport = true) }
        launch(onError = {
            _uiState.update { it.copy(isSubmittingReport = false) }
            handleError(it)
        }) {
            val result = reportUserUseCase(userId, reasonString)
            _uiState.update { it.copy(isSubmittingReport = false, showReportSheet = false, selectedReportReason = null, otherReportReasonText = "") }
            when (result) {
                is AppResult.Success -> showSuccess(UiText.Resource(R.string.user_profile_report_submitted))
                is AppResult.Error -> showError(result.error.toUiText())
            }
        }
    }
}
