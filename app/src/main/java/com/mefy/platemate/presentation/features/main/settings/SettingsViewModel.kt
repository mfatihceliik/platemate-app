package com.mefy.platemate.presentation.features.main.settings

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.auth.UserRole
import com.mefy.platemate.domain.usecase.auth.LogoutUseCase
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.domain.usecase.language.ObserveLanguageUseCase
import com.mefy.platemate.domain.usecase.theme.ObserveThemeModeUseCase
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
import kotlinx.coroutines.flow.update

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeThemeModeUseCase: ObserveThemeModeUseCase,
    private val observeLanguageUseCase: ObserveLanguageUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SettingsUiEffect>()
    val uiEffect: SharedFlow<SettingsUiEffect> = _uiEffect.asSharedFlow()

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
        launch {
            observeSessionUseCase().collect { session ->
                _uiState.update { it.copy(isAdmin = session?.role == UserRole.ADMIN) }
            }
        }
    }

    private fun AppLanguage.toLabel(): UiText = UiText.Resource(
        when (this) {
            AppLanguage.TR -> R.string.profile_setting_language_tr
            AppLanguage.EN -> R.string.profile_setting_language_en
        }
    )

    fun onAction(action: SettingsUiAction) {
        when (action) {
            SettingsUiAction.ChangePasswordClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToChangePassword)

            SettingsUiAction.EditProfileClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToEditProfile)

            SettingsUiAction.PremiumClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToPremium)

            SettingsUiAction.ThemeColorClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToThemeColor)

            SettingsUiAction.CardStyleClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToCardStyle)

            SettingsUiAction.LanguageClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToLanguage)

            SettingsUiAction.NotificationPreferencesClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToNotificationPreferences)

            SettingsUiAction.AdminPanelClicked ->
                _uiEffect.emitUiEffect(SettingsUiEffect.NavigateToAdmin)

            SettingsUiAction.SignOutClicked -> onSignOutClicked()

            SettingsUiAction.RetryClicked -> loadSettingsOverview()
        }
    }

    private fun loadSettingsOverview() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(
            onError = {
                _uiState.update { state ->
                    state.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown))
                }
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
                    _uiState.update { state -> state.copy(isLoading = false, errorMessage = result.error.toUiText()) }
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
