package com.mefy.platemate.presentation.features.admin.settings

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.admin.GetAppSettingsUseCase
import com.mefy.platemate.domain.usecase.admin.UpdateAppSettingsUseCase
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
class AdminSettingsViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private companion object {
        const val MAX_DIGITS = 4
    }

    private val _uiState = MutableStateFlow(AdminSettingsUiState())
    val uiState: StateFlow<AdminSettingsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AdminSettingsUiEffect>()
    val uiEffect: SharedFlow<AdminSettingsUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: AdminSettingsUiAction) {
        when (action) {
            AdminSettingsUiAction.BackClicked -> _uiEffect.emitUiEffect(AdminSettingsUiEffect.NavigateBack)
            AdminSettingsUiAction.SaveClicked -> save()
            is AdminSettingsUiAction.FollowLimitChanged ->
                _uiState.update { it.copy(followLimit = action.value.digitsOnly()) }
            is AdminSettingsUiAction.AlarmLimitChanged ->
                _uiState.update { it.copy(alarmLimit = action.value.digitsOnly()) }
            is AdminSettingsUiAction.MessageLimitChanged ->
                _uiState.update { it.copy(messageLimit = action.value.digitsOnly()) }
            is AdminSettingsUiAction.ReportThresholdChanged ->
                _uiState.update { it.copy(reportThreshold = action.value.digitsOnly()) }
            is AdminSettingsUiAction.CommentMaxLengthChanged ->
                _uiState.update { it.copy(commentMaxLength = action.value.digitsOnly()) }
        }
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = { error ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(error)
        }) {
            when (val result = getAppSettingsUseCase()) {
                is AppResult.Success -> {
                    val settings = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            followLimit = settings.nonPremiumPlateFollowLimit.toString(),
                            alarmLimit = settings.nonPremiumPlateAlarmLimit.toString(),
                            messageLimit = settings.preApprovalMessageLimit.toString(),
                            reportThreshold = settings.commentReportThreshold.toString(),
                            commentMaxLength = settings.reportCommentMaxLength.toString()
                        )
                    }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (!state.isSaveEnabled) return
        val follow = state.followLimit.toIntOrNull() ?: return
        val alarm = state.alarmLimit.toIntOrNull() ?: return
        val message = state.messageLimit.toIntOrNull() ?: return
        val threshold = state.reportThreshold.toIntOrNull() ?: return
        val commentMaxLength = state.commentMaxLength.toIntOrNull() ?: return

        _uiState.update { it.copy(isSaving = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isSaving = false) }
            handleError(error)
        }) {
            when (val result = updateAppSettingsUseCase(follow, alarm, message, threshold, commentMaxLength)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showSuccess(UiText.Resource(R.string.admin_settings_saved))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }
    private fun String.digitsOnly(): String = filter { it.isDigit() }.take(MAX_DIGITS)
}
