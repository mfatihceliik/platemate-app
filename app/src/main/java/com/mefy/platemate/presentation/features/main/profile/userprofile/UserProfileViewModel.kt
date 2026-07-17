package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.repository.SocialRepository
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.report.ReportUserUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.presentation.common.avatar.AvatarPalette
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackBg
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackTint
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
                is AppResult.Success -> _uiState.update { it.applyProfile(result.data, platforms) }
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
        _uiState.update { it.copy(friendshipStatus = "PENDING_SENT") }
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
        _uiState.update { it.copy(friendshipStatus = "FRIENDS") }
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
        _uiState.update { it.copy(friendshipStatus = "NONE", showRemoveFriendPopup = false) }
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

    private fun UserProfileUiState.applyProfile(
        profile: UserProfile,
        platforms: List<SocialPlatform>
    ): UserProfileUiState {
        val display = profile.displayName?.takeIf { it.isNotBlank() } ?: profile.username
        return copy(
            isLoading = false,
            displayName = display,
            username = "@${profile.username}",
            bio = profile.bio.orEmpty(),
            isVerified = profile.verified,
            isOnline = false,
            friendshipStatus = profile.friendshipStatus,
            friendshipId = profile.friendshipId,
            reviewCount = profile.reviewCount,
            followerCount = NumberFormatter.formatCompact(profile.followerCount.toLong()),
            followingCount = profile.followingCount,
            socialLinks = profile.socialMediaLinks.map { link ->
                val platform = platforms.find { it.code.equals(link.platform, ignoreCase = true) }
                ProfileSocialLinkUiModel(
                    id = link.id,
                    platform = link.platform,
                    url = link.url,
                    iconUrl = platform?.iconUrl,
                    backgroundColor = platform?.backgroundColor ?: SocialPlatformFallbackBg,
                    iconTint = platform?.iconTint ?: SocialPlatformFallbackTint
                )
            },
            approvedReviews = profile.plateReviews.map { it.toReviewUiModel() }
        )
    }

    private fun Review.toReviewUiModel(): UserProfileReviewUiModel = UserProfileReviewUiModel(
        id = id,
        plateCode = plateCode.take(2),
        plateNumber = plateCode,
        city = CityNameResolver.resolveCityName(cityName = null, plateCode = plateCode).orEmpty(),
        date = createdAt?.iso8601?.substringBefore("T").orEmpty(),
        rating = rating.toFloat(),
        tags = emptyList(),
        comment = comment
    )
}
