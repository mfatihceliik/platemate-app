package com.mefy.platemate.presentation.features.main.profile.settings.sociallinks

import com.mefy.platemate.R
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.domain.usecase.sociallink.AddSocialLinkUseCase
import com.mefy.platemate.domain.usecase.sociallink.DeleteSocialLinkUseCase
import com.mefy.platemate.domain.usecase.sociallink.UpdateSocialLinkUseCase
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class SocialLinksViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val addSocialLinkUseCase: AddSocialLinkUseCase,
    private val updateSocialLinkUseCase: UpdateSocialLinkUseCase,
    private val deleteSocialLinkUseCase: DeleteSocialLinkUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    companion object {
        val supportedPlatforms: List<String> = listOf(
            "INSTAGRAM",
            "X",
            "FACEBOOK"
        )
    }

    private val _uiState = MutableStateFlow(SocialLinksUiState())
    val uiState: StateFlow<SocialLinksUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SocialLinksUiEffect>()
    val uiEffect: SharedFlow<SocialLinksUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: SocialLinksUiAction) {
        when (action) {
            is SocialLinksUiAction.ExistingUrlChanged -> {
                _uiState.update { state ->
                    state.copy(
                        links = state.links.map { link ->
                            if (link.id == action.id) link.copy(url = action.url) else link
                        }
                    )
                }
            }

            is SocialLinksUiAction.NewPlatformSelected -> {
                _uiState.update { it.copy(selectedPlatform = action.platform) }
            }

            is SocialLinksUiAction.NewUrlChanged -> {
                _uiState.update { it.copy(newUrl = action.url) }
            }

            is SocialLinksUiAction.UpdateClicked -> updateLink(action.id)
            is SocialLinksUiAction.DeleteClicked -> deleteLink(action.id)
            SocialLinksUiAction.AddClicked -> addLink()
        }
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = ::handleError) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            links = result.data.socialMediaLinks.mapNotNull { link ->
                                val id = link.id ?: return@mapNotNull null
                                SocialLinkEditorUiModel(
                                    id = id,
                                    platform = link.platform,
                                    url = link.url
                                )
                            }
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

    private fun addLink() {
        val state = _uiState.value
        val url = state.newUrl.trim()
        if (url.isEmpty() || state.isSubmitting) return

        _uiState.update { it.copy(isSubmitting = true) }
        launch(onError = ::handleError) {
            when (val result = addSocialLinkUseCase(state.selectedPlatform, url)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(newUrl = "", isSubmitting = false) }
                    load()
                    _uiEffect.emitUiEffect(
                        SocialLinksUiEffect.ShowSnackbar(UiText.Resource(R.string.profile_social_link_added))
                    )
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update { it.copy(isSubmitting = false) }
                }
            }
        }
    }

    private fun updateLink(id: Long) {
        val state = _uiState.value
        val link = state.links.firstOrNull { it.id == id } ?: return

        _uiState.update {
            it.copy(
                links = it.links.map { item ->
                    if (item.id == id) item.copy(isSaving = true) else item
                }
            )
        }

        launch(onError = ::handleError) {
            when (
                val result = updateSocialLinkUseCase(
                    SocialMediaLink(
                        id = link.id,
                        platform = link.platform,
                        url = link.url.trim()
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            links = it.links.map { item ->
                                if (item.id == id) item.copy(isSaving = false) else item
                            }
                        )
                    }
                    _uiEffect.emitUiEffect(
                        SocialLinksUiEffect.ShowSnackbar(UiText.Resource(R.string.profile_settings_saved))
                    )
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update {
                        it.copy(
                            links = it.links.map { item ->
                                if (item.id == id) item.copy(isSaving = false) else item
                            }
                        )
                    }
                }
            }
        }
    }

    private fun deleteLink(id: Long) {
        _uiState.update {
            it.copy(
                links = it.links.map { item ->
                    if (item.id == id) item.copy(isDeleting = true) else item
                }
            )
        }

        launch(onError = ::handleError) {
            when (val result = deleteSocialLinkUseCase(id)) {
                is AppResult.Success -> {
                    _uiState.update { state ->
                        state.copy(links = state.links.filterNot { it.id == id })
                    }
                    _uiEffect.emitUiEffect(
                        SocialLinksUiEffect.ShowSnackbar(UiText.Resource(R.string.profile_social_link_deleted))
                    )
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update {
                        it.copy(
                            links = it.links.map { item ->
                                if (item.id == id) item.copy(isDeleting = false) else item
                            }
                        )
                    }
                }
            }
        }
    }
}
