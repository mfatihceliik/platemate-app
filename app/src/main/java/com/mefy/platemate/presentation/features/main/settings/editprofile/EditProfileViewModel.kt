package com.mefy.platemate.presentation.features.main.settings.editprofile

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.usecase.auth.LogoutUseCase
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.profile.UpdateProfileUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.domain.usecase.user.DeleteAccountUseCase
import com.mefy.platemate.domain.repository.SocialLinkRepository
import com.mefy.platemate.presentation.features.main.settings.editprofile.model.toUiModel
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
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
class EditProfileViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getSocialPlatformsUseCase: GetSocialPlatformsUseCase,
    private val socialLinkRepository: SocialLinkRepository,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val logoutUseCase: LogoutUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<EditProfileUiEffect>()
    val uiEffect: SharedFlow<EditProfileUiEffect> = _uiEffect.asSharedFlow()
    private var currentUserId: Long? = null
    private var loadedLinks: List<SocialMediaLink> = emptyList()

    init {
        loadProfile()
    }

    fun onAction(action: EditProfileUiAction) {
        when (action) {
            EditProfileUiAction.BackClicked -> _uiEffect.emitUiEffect(EditProfileUiEffect.NavigateBack)
            EditProfileUiAction.SaveClicked -> saveProfile()
            EditProfileUiAction.RetryClicked -> loadProfile()
            EditProfileUiAction.AvatarEditClicked -> _uiState.update { it.copy(showAvatarDialog = true, avatarUrlDraft = it.profilePhotoUrl) }
            is EditProfileUiAction.AvatarUrlChanged -> _uiState.update { it.copy(avatarUrlDraft = action.value) }
            EditProfileUiAction.AvatarUrlConfirmed -> _uiState.update { it.copy(profilePhotoUrl = it.avatarUrlDraft.trim(), showAvatarDialog = false) }
            EditProfileUiAction.AvatarDialogDismissed -> _uiState.update { it.copy(showAvatarDialog = false) }
            EditProfileUiAction.DeleteAccountClicked -> deleteAccount()
            is EditProfileUiAction.DisplayNameChanged -> _uiState.update { it.copy(displayName = action.value, displayNameError = null) }
            is EditProfileUiAction.UsernameChanged -> _uiState.update { it.copy(username = action.value, usernameError = null) }
            is EditProfileUiAction.BioChanged -> _uiState.update { it.copy(bio = action.value.take(it.bioMaxLength)) }
            is EditProfileUiAction.SocialLinkChanged -> _uiState.value.socialLinks[action.platformId] = action.value

        }
    }
    private fun loadProfile() {
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
            currentUserId = userId

            val platforms = when (val platformsResult = getSocialPlatformsUseCase()) {
                is AppResult.Success -> platformsResult.data.map { it.toUiModel() }
                is AppResult.Error -> emptyList()
            }

            when (val result = getProfileUseCase(userId)) {
                is AppResult.Success -> {
                    val profile = result.data
                    loadedLinks = profile.socialMediaLinks
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            displayName = profile.displayName ?: profile.username,
                            username = profile.username,
                            bio = profile.bio.orEmpty(),
                            profilePhotoUrl = profile.profilePhotoUrl.orEmpty(),
                            availablePlatforms = platforms,
                        )
                    }
                    _uiState.value.socialLinks.clear()
                    loadedLinks.forEach { link -> _uiState.value.socialLinks[link.platform.uppercase()] = link.url }
                }
                is AppResult.Error ->
                    _uiState.update { it.copy(isLoading = false, availablePlatforms = platforms, errorMessage = result.error.toUiText()) }
            }
        }
    }

    private fun saveProfile() {
        val state = _uiState.value
        if (!validate(state)) return
        val userId = currentUserId ?: return

        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            val result = updateProfileUseCase(
                userId = userId,
                displayName = state.displayName.trim(),
                username = state.username.trim(),
                bio = state.bio.trim(),
                profilePhotoUrl = state.profilePhotoUrl.trim().ifBlank { null }
            )
            when (result) {
                is AppResult.Success -> {
                    syncSocialLinks(state)
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.edit_profile_saved))
                    _uiEffect.emitUiEffect(EditProfileUiEffect.NavigateBack)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private suspend fun syncSocialLinks(state: EditProfileUiState) {
        val desired = state.socialLinks.mapValues { it.value.trim() }
        desired.forEach { (platform, url) ->
            val existing = loadedLinks.firstOrNull {
                    it.platform.equals(platform, true)
                }
            when {
                url.isNotBlank() && existing == null -> socialLinkRepository.addSocialLink(platform, url)
                url.isNotBlank() && existing != null && existing.url != url -> socialLinkRepository.updateSocialLink(existing.copy(url = url))
                url.isBlank() && existing?.id != null -> socialLinkRepository.deleteSocialLink(existing.id)
            }
        }
    }

    private fun deleteAccount() {
        launch(onError = ::handleError) {
            when (deleteAccountUseCase()) {
                is AppResult.Success -> logoutUseCase() // session cleared -> app routes to auth
                is AppResult.Error -> showError(UiText.Resource(R.string.common_error_unknown))
            }
        }
    }

    private fun validate(state: EditProfileUiState): Boolean {
        var valid = true
        if (state.displayName.isBlank()) {
            _uiState.update { it.copy(displayNameError = UiText.Resource(R.string.edit_profile_validation_display_name_required)) }
            valid = false
        }
        if (state.username.isBlank()) {
            _uiState.update { it.copy(usernameError = UiText.Resource(R.string.edit_profile_validation_username_required)) }
            valid = false
        }
        return valid
    }

}
