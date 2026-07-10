package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.usecase.follow.FollowUserUseCase
import com.mefy.platemate.domain.usecase.follow.UnfollowUserUseCase
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
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
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

            UserProfileUiAction.FollowClicked -> onFollowClicked()

            UserProfileUiAction.MessageClicked -> onMessageClicked()

            UserProfileUiAction.ShareClicked -> Unit

            UserProfileUiAction.ReportMenuClicked ->
                _uiState.update { it.copy(showReportSheet = true) }

            is UserProfileUiAction.ReportReasonSelected ->
                _uiState.update { it.copy(selectedReportReason = action.reason) }

            UserProfileUiAction.ReportSubmitClicked -> submitReport()

            UserProfileUiAction.ReportDismissed ->
                _uiState.update { it.copy(showReportSheet = false, selectedReportReason = null) }
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
        // Oda burada YARATILMAZ; taslak konuşma açılır, gerçek oda ilk mesaj
        // gönderiminde (ConversationViewModel) oluşturulur. Böylece mesaj atılmadan
        // boş sohbet kalmaz.
        val state = _uiState.value
        _uiEffect.emitUiEffect(
            UserProfileUiEffect.NavigateToChat(
                conversationId = "",
                otherUserId = userId,
                participantName = state.displayName,
            )
        )
    }

    private fun onFollowClicked() {
        val wasFollowing = _uiState.value.isFollowing
        _uiState.update { it.copy(isFollowing = !wasFollowing) }
        launch(onError = {
            _uiState.update { state -> state.copy(isFollowing = wasFollowing) }
            handleError(it)
        }) {
            val result = if (wasFollowing) unfollowUserUseCase(userId) else followUserUseCase(userId)
            if (result is AppResult.Error) {
                _uiState.update { it.copy(isFollowing = wasFollowing) }
                showError(result.error.toUiText())
            }
        }
    }

    private fun submitReport() {
        val reason = _uiState.value.selectedReportReason ?: return
        _uiState.update { it.copy(isSubmittingReport = true) }
        launch(onError = {
            _uiState.update { it.copy(isSubmittingReport = false) }
            handleError(it)
        }) {
            val result = reportUserUseCase(userId, reason.name)
            _uiState.update { it.copy(isSubmittingReport = false, showReportSheet = false, selectedReportReason = null) }
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
        val (background, foreground) = AvatarPalette.colorsFor(profile.id)
        return copy(
            isLoading = false,
            displayName = display,
            username = "@${profile.username}",
            initials = AvatarPalette.initials(display),
            avatarBg = background,
            avatarFg = foreground,
            bio = profile.bio.orEmpty(),
            isVerified = profile.verified,
            isOnline = false,
            isFollowing = profile.isFollowing,
            reviewCount = profile.reviewCount,
            followerCount = NumberFormatter.formatCompact(profile.followerCount.toLong()),
            followingCount = profile.followingCount,
            socialLinks = profile.socialMediaLinks.map { link ->
                // Bilinmeyen platformlar jenerik link ikonuna düşer (kendi profilinden farklı
                // olarak başka kullanıcının verisi keyfî olabilir; !! ile çökmek yerine fallback).
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
