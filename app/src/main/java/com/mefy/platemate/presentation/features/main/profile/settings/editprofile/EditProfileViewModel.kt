package com.mefy.platemate.presentation.features.main.profile.settings.editprofile

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
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
class EditProfileViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<EditProfileUiEffect>()
    val uiEffect: SharedFlow<EditProfileUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadProfile()
    }

    fun onAction(action: EditProfileUiAction) {
        when (action) {
            EditProfileUiAction.BackClicked ->
                _uiEffect.emitUiEffect(EditProfileUiEffect.NavigateBack)

            EditProfileUiAction.SaveClicked -> saveProfile()
            EditProfileUiAction.AvatarEditClicked -> Unit
            EditProfileUiAction.DeleteAccountClicked -> Unit

            is EditProfileUiAction.DisplayNameChanged ->
                _uiState.update { it.copy(displayName = action.value, displayNameError = null) }

            is EditProfileUiAction.UsernameChanged ->
                _uiState.update { it.copy(username = action.value, usernameError = null) }

            is EditProfileUiAction.BioChanged ->
                _uiState.update {
                    it.copy(bio = action.value.take(it.bioMaxLength))
                }

            is EditProfileUiAction.InstagramChanged ->
                _uiState.update { it.copy(instagramUrl = action.value) }

            is EditProfileUiAction.TwitterChanged ->
                _uiState.update { it.copy(twitterUrl = action.value) }

            is EditProfileUiAction.LinkedInChanged ->
                _uiState.update { it.copy(linkedInUrl = action.value) }

            is EditProfileUiAction.WebsiteChanged ->
                _uiState.update { it.copy(websiteUrl = action.value) }
        }
    }

    private fun loadProfile() {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = { error ->
            handleError(error)
            _uiState.update { it.copy(isLoading = false) }
        }) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    val overview = result.data
                    val links = overview.socialMediaLinks
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            username = overview.username ?: "",
                            displayName = overview.username ?: "",
                            instagramUrl = links.firstOrNull { l -> l.platform.equals("INSTAGRAM", ignoreCase = true) }?.url ?: "",
                            twitterUrl = links.firstOrNull { l -> l.platform.equals("X", ignoreCase = true) || l.platform.equals("TWITTER", ignoreCase = true) }?.url ?: "",
                            linkedInUrl = links.firstOrNull { l -> l.platform.equals("LINKEDIN", ignoreCase = true) }?.url ?: "",
                            websiteUrl = links.firstOrNull { l -> l.platform.equals("WEBSITE", ignoreCase = true) }?.url ?: ""
                        )
                    }
                }
                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun saveProfile() {
        val state = _uiState.value
        if (!validate(state)) return

        // TODO: inject and call UpdateProfileUseCase when backend endpoint is ready
        _uiEffect.emitUiEffect(EditProfileUiEffect.ShowSnackbar("Profil güncellendi"))
        _uiEffect.emitUiEffect(EditProfileUiEffect.NavigateBack)
    }

    private fun validate(state: EditProfileUiState): Boolean {
        var valid = true
        if (state.displayName.isBlank()) {
            _uiState.update { it.copy(displayNameError = "Ad Soyad boş bırakılamaz") }
            valid = false
        }
        if (state.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Kullanıcı adı boş bırakılamaz") }
            valid = false
        }
        return valid
    }
}
