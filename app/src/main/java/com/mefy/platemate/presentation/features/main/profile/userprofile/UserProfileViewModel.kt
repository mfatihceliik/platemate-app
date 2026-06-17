package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.ReportReason
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileReviewUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileSocialLinkUiModel
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
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val route: UserProfileDestination = savedStateHandle.toRoute()

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

            UserProfileUiAction.FollowClicked ->
                _uiState.update { it.copy(isFollowing = !it.isFollowing) }

            UserProfileUiAction.MessageClicked ->
                _uiEffect.emitUiEffect(UserProfileUiEffect.NavigateToChat(route.userId))

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
        // TODO: replace with real GetUserProfileUseCase(route.userId) when backend endpoint is ready
        _uiState.update {
            it.copy(
                isLoading = false,
                displayName = "Ahmet Yılmaz",
                username = "@ahmet.yilmaz",
                initials = "AY",
                avatarBg = Color(0xFFECFEFF),
                avatarFg = Color(0xFF0E7490),
                bio = "İstanbul'dan 🚗 Plaka meraklısı. İyi sürücüleri fark ediyorum.",
                isVerified = true,
                isOnline = true,
                isFollowing = false,
                reviewCount = 128,
                followerCount = "1.2K",
                followingCount = 84,
                socialLinks = listOf(
                    UserProfileSocialLinkUiModel("INSTAGRAM", "https://instagram.com/ahmetyilmaz"),
                    UserProfileSocialLinkUiModel("X", "https://twitter.com/ahmetyilmaz"),
                    UserProfileSocialLinkUiModel("LINKEDIN", "https://linkedin.com/in/ahmetyilmaz"),
                    UserProfileSocialLinkUiModel("WEBSITE", "https://ahmetyilmaz.com")
                ),
                approvedReviews = listOf(
                    UserProfileReviewUiModel(
                        id = 1L,
                        plateCode = "34",
                        plateNumber = "34 EK 0682",
                        city = "İstanbul",
                        date = "2 gün önce",
                        rating = 5.0f,
                        tags = listOf("Nazik Sürüş", "Güvenli"),
                        comment = "Sinyali her zaman kullanan, yayalara öncelik veren örnek bir sürücü. Kesinlikle 5 yıldız!"
                    ),
                    UserProfileReviewUiModel(
                        id = 2L,
                        plateCode = "06",
                        plateNumber = "06 ABC 123",
                        city = "Ankara",
                        date = "1 hafta önce",
                        rating = 4.5f,
                        tags = listOf("Hızlı Değil", "Saygılı"),
                        comment = "Trafik ışığında bekleyen, kimseyi geçmeye zorlamayan nazik bir araç."
                    )
                )
            )
        }
    }

    private fun submitReport() {
        val reason = _uiState.value.selectedReportReason ?: return
        // TODO: call ReportUserUseCase(route.userId, reason) when backend is ready
        _uiState.update { it.copy(showReportSheet = false, selectedReportReason = null) }
    }
}
