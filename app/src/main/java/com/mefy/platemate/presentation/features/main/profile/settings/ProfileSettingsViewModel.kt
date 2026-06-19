package com.mefy.platemate.presentation.features.main.profile.settings

import com.mefy.platemate.R
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.usecase.auth.LogoutUseCase
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.domain.usecase.language.ObserveLanguageUseCase
import com.mefy.platemate.domain.usecase.theme.ObserveThemeModeUseCase
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
class ProfileSettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val observeLanguageUseCase: ObserveLanguageUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileSettingsUiEffect>()
    val uiEffect: SharedFlow<ProfileSettingsUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadSettingsOverview()
        observePreferences()
    }

    private fun observePreferences() {
        launch {
            observeThemeModeUseCase().collect { themeMode ->
                _uiState.update { it.copy(themeMode = themeMode) }
            }
        }
        launch {
            observeLanguageUseCase().collect { language ->
                val resolved = language ?: AppLanguage.TR
                _uiState.update { it.copy(language = resolved, languageLabel = resolved.toLabel()) }
            }
        }
    }

    private fun AppLanguage.toLabel(): UiText = UiText.Resource(
        when (this) {
            AppLanguage.TR -> R.string.profile_setting_language_tr
            AppLanguage.EN -> R.string.profile_setting_language_en
        }
    )

    fun onAction(action: ProfileSettingsUiAction) {
        when (action) {
            ProfileSettingsUiAction.ChangePasswordClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToChangePassword)

            ProfileSettingsUiAction.EditProfileClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToEditProfile)

            ProfileSettingsUiAction.PremiumClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToPremium)

            ProfileSettingsUiAction.ThemeColorClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToThemeColor)

            ProfileSettingsUiAction.LanguageClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToLanguage)

            ProfileSettingsUiAction.NotificationPreferencesClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToNotificationPreferences)

            ProfileSettingsUiAction.SocialLinksClicked ->
                _uiEffect.emitUiEffect(ProfileSettingsUiEffect.NavigateToSocialLinks)

            ProfileSettingsUiAction.SignOutClicked -> onSignOutClicked()
        }
    }

    private fun loadSettingsOverview() {
        _uiState.update { it.copy(isLoading = true) }
        launch(
            onError = {
                _uiState.update { state -> state.copy(isLoading = false) }
                handleError(it)
            }
        ) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    val overview = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            premiumActive = overview.premiumActive
                        )
                    }
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update { state -> state.copy(isLoading = false) }
                }
            }
        }
    }

    private fun onSignOutClicked() {
        launch(onError = ::handleError) {
            logoutUseCase()
        }
    }
}
