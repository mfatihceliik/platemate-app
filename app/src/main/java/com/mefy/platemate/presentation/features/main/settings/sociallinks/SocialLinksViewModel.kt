package com.mefy.platemate.presentation.features.main.settings.sociallinks

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.sociallink.AddSocialLinkUseCase
import com.mefy.platemate.domain.usecase.sociallink.DeleteSocialLinkUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatform
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatformFallbackBg
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.SocialPlatformFallbackTint
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

@HiltViewModel
class SocialLinksViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getSocialPlatformsUseCase: GetSocialPlatformsUseCase,
    private val addSocialLinkUseCase: AddSocialLinkUseCase,
    private val deleteSocialLinkUseCase: DeleteSocialLinkUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(SocialLinksUiState())
    val uiState: StateFlow<SocialLinksUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SocialLinksUiEffect>()
    val uiEffect: SharedFlow<SocialLinksUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadLinks()
    }

    fun onAction(action: SocialLinksUiAction) {
        when (action) {
            SocialLinksUiAction.BackClicked ->
                _uiEffect.emitUiEffect(SocialLinksUiEffect.NavigateBack)

            SocialLinksUiAction.RetryClicked -> loadLinks()

            is SocialLinksUiAction.PlatformSelected ->
                _uiState.update { it.copy(selectedPlatformId = action.platformId) }

            is SocialLinksUiAction.UrlChanged ->
                _uiState.update { it.copy(urlInput = action.value) }

            SocialLinksUiAction.AddClicked -> addLink()
            is SocialLinksUiAction.DeleteClicked -> deleteLink(action.linkId)
        }
    }

    private fun loadLinks() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            val userId = observeSessionUseCase().first()?.userId
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
                return@launch
            }
            val platforms = when (val platformsResult = getSocialPlatformsUseCase()) {
                is AppResult.Success -> platformsResult.data.map { it.toUiModel() }
                is AppResult.Error -> emptyList()
            }
            when (val result = getProfileUseCase(userId)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        availablePlatforms = platforms,
                        links = result.data.socialMediaLinks.map { link -> toUiModel(link, platforms) }
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, availablePlatforms = platforms, errorMessage = result.error.toUiText())
                }
            }
        }
    }

    private fun addLink() {
        val state = _uiState.value
        val platformId = state.selectedPlatformId ?: return
        val url = state.urlInput.trim()
        if (url.isBlank() || state.isSubmitting) return

        _uiState.update { it.copy(isSubmitting = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSubmitting = false) }
            handleError(error)
        }) {
            when (val result = addSocialLinkUseCase(platformId, url)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false, selectedPlatformId = null, urlInput = "") }
                    showSuccess(UiText.Resource(R.string.profile_social_link_added))
                    loadLinks()
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun deleteLink(linkId: Long) {
        launch(onError = ::handleError) {
            when (val result = deleteSocialLinkUseCase(linkId)) {
                is AppResult.Success -> {
                    showSuccess(UiText.Resource(R.string.profile_social_link_deleted))
                    loadLinks()
                }
                is AppResult.Error -> showError(result.error.toUiText())
            }
        }
    }

    private fun toUiModel(link: SocialMediaLink, platforms: List<SocialPlatform>): ProfileSocialLinkUiModel {
        // Bilinmeyen platform jenerik link görünümüne düşer; !! ile çökmek yerine fallback.
        val platform = platforms.find { it.code.equals(link.platform, ignoreCase = true) }
        return ProfileSocialLinkUiModel(
            id = link.id,
            platform = link.platform,
            url = link.url,
            iconUrl = platform?.iconUrl,
            backgroundColor = platform?.backgroundColor ?: SocialPlatformFallbackBg,
            iconTint = platform?.iconTint ?: SocialPlatformFallbackTint
        )
    }
}
